package io.github.kei_1111.withmo.ui.screens.favorite_app_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.FavoriteOrder
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import io.github.kei_1111.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteAppSettingsViewModel @Inject constructor(
    private val appInfoRepository: AppInfoRepository,
    private val getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
) : BaseViewModel<FavoriteAppSettingsUiState, FavoriteAppSettingsUiEvent>() {

    override fun createInitialState(): FavoriteAppSettingsUiState = FavoriteAppSettingsUiState()

    val appList: StateFlow<List<AppInfo>> = appInfoRepository.getAllAppInfoList()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(TimeoutMillis), initialValue = emptyList())

    init {
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { favoriteAppList ->
                val sortedFavoriteAppList = favoriteAppList.toPersistentList()

                _uiState.update {
                    it.copy(
                        favoriteAppList = sortedFavoriteAppList,
                        initialFavoriteAppList = sortedFavoriteAppList,
                    )
                }
            }
        }

        viewModelScope.launch {
            getAppIconSettingsUseCase().collect { appIconSettings ->
                _uiState.update {
                    it.copy(appIconSettings = appIconSettings)
                }
            }
        }
    }

    fun addFavoriteAppList(appInfo: AppInfo) {
        val addedFavoriteAppList = (_uiState.value.favoriteAppList + appInfo).toPersistentList()

        _uiState.update { currentState ->
            if (currentState.favoriteAppList.size < UiConfig.FavoriteAppListMaxSize &&
                currentState.favoriteAppList.none { it.packageName == appInfo.packageName }
            ) {
                currentState.copy(
                    favoriteAppList = addedFavoriteAppList,
                    isSaveButtonEnabled = currentState.initialFavoriteAppList != addedFavoriteAppList,
                )
            } else {
                currentState
            }
        }
    }

    fun removeFavoriteAppList(appInfo: AppInfo) {
        val removedFavoriteAppList = _uiState.value.favoriteAppList.filterNot { it.packageName == appInfo.packageName }
            .toPersistentList()

        _uiState.update {
            it.copy(
                favoriteAppList = removedFavoriteAppList,
                isSaveButtonEnabled = (
                    it.initialFavoriteAppList != removedFavoriteAppList &&
                        removedFavoriteAppList.size > 0
                    ),
            )
        }
    }

    fun onValueChangeAppSearchQuery(query: String) {
        _uiState.update {
            it.copy(appSearchQuery = query)
        }
    }

    fun saveFavoriteAppList() {
        val currentFavoriteAppList = _uiState.value.favoriteAppList
        val initialFavoriteAppList = _uiState.value.initialFavoriteAppList

        val favoriteOrders = listOf(
            FavoriteOrder.First,
            FavoriteOrder.Second,
            FavoriteOrder.Third,
            FavoriteOrder.Fourth,
        )

        val currentPackageNames = currentFavoriteAppList.map { it.packageName }.toSet()

        val appsToRemoveFromFavorites = initialFavoriteAppList.filter { appInfo ->
            appInfo.favoriteOrder != FavoriteOrder.NotFavorite && !currentPackageNames.contains(appInfo.packageName)
        }.map { appInfo ->
            appInfo.copy(favoriteOrder = FavoriteOrder.NotFavorite)
        }

        val appsToUpdateFavorites = currentFavoriteAppList.mapIndexed { index, appInfo ->
            appInfo.copy(
                favoriteOrder = favoriteOrders.getOrNull(index) ?: FavoriteOrder.NotFavorite,
            )
        }

        val appsToUpdate = appsToUpdateFavorites + appsToRemoveFromFavorites

        _uiState.update {
            it.copy(isSaveButtonEnabled = false)
        }

        viewModelScope.launch {
            try {
                appInfoRepository.updateAppInfoList(appsToUpdate)
                _uiEvent.emit(FavoriteAppSettingsUiEvent.SaveSuccess)
            } catch (e: Exception) {
                Log.e("FavoriteAppSettingsViewModel", "お気に入りアプリの保存に失敗しました", e)
                _uiEvent.emit(FavoriteAppSettingsUiEvent.SaveFailure)
            }
        }
    }

    companion object {
        private const val TimeoutMillis = 5000L
    }
}
