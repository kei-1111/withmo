package com.example.withmo.ui.screens.favorite_app_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.FavoriteOrder
import com.example.withmo.domain.repository.AppInfoRepository
import com.example.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCase
import com.example.withmo.ui.base.BaseViewModel
import com.example.withmo.ui.theme.UiConfig
import dagger.hilt.android.lifecycle.HiltViewModel
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
                val sortedFavoriteAppList = favoriteAppList.sortedBy { it.favoriteOrder.ordinal }.toPersistentList()

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

        val appsToRemove = initialFavoriteAppList.filterNot { appInfo ->
            currentFavoriteAppList.any { it.packageName == appInfo.packageName }
        }.map { appInfo ->
            appInfo.copy(favoriteOrder = FavoriteOrder.NotFavorite)
        }

        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }

        viewModelScope.launch {
            try {
                appsToRemove.forEach {
                    appInfoRepository.updateAppInfo(it)
                }
                currentFavoriteAppList.forEachIndexed { index, appInfo ->
                    appInfoRepository.updateAppInfo(
                        appInfo.copy(
                            favoriteOrder = when (index) {
                                0 -> FavoriteOrder.First
                                1 -> FavoriteOrder.Second
                                2 -> FavoriteOrder.Third
                                3 -> FavoriteOrder.Fourth
                                else -> FavoriteOrder.NotFavorite
                            },
                        ),
                    )
                }
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
