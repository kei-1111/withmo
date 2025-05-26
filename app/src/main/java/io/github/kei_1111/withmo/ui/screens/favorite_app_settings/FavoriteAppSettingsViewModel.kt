package io.github.kei_1111.withmo.ui.screens.favorite_app_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.common.AppConstants
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.FavoriteOrder
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteAppSettingsViewModel @Inject constructor(
    private val appInfoRepository: AppInfoRepository,
    private val getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
) : BaseViewModel<FavoriteAppSettingsState, FavoriteAppSettingsAction, FavoriteAppSettingsEffect>() {

    override fun createInitialState(): FavoriteAppSettingsState = FavoriteAppSettingsState()

    val appList: StateFlow<List<AppInfo>> = appInfoRepository.getAllAppInfoList()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(TimeoutMillis), initialValue = emptyList())

    init {
        observeFavoriteAppList()

        observeAppIconSettings()
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { favoriteAppList ->
                val sortedFavoriteAppList = favoriteAppList.toPersistentList()

                updateState {
                    copy(
                        favoriteAppList = sortedFavoriteAppList,
                        initialFavoriteAppList = sortedFavoriteAppList,
                    )
                }
            }
        }
    }

    private fun observeAppIconSettings() {
        viewModelScope.launch {
            getAppIconSettingsUseCase().collect { appIconSettings ->
                updateState { copy(appIconSettings = appIconSettings) }
            }
        }
    }

    private fun addFavoriteAppList(appInfo: AppInfo) {
        val addedFavoriteAppList = (state.value.favoriteAppList + appInfo).toPersistentList()

        updateState {
            if (favoriteAppList.size < AppConstants.FavoriteAppListMaxSize &&
                favoriteAppList.none { it.packageName == appInfo.packageName }
            ) {
                copy(
                    favoriteAppList = addedFavoriteAppList,
                    isSaveButtonEnabled = !addedFavoriteAppList.isSameAs(initialFavoriteAppList),
                )
            } else {
                this
            }
        }
    }

    private fun removeFavoriteAppList(appInfo: AppInfo) {
        val removedFavoriteAppList = state.value.favoriteAppList
            .filterNot { it.packageName == appInfo.packageName }
            .toPersistentList()

        updateState {
            copy(
                favoriteAppList = removedFavoriteAppList,
                isSaveButtonEnabled = (initialFavoriteAppList != removedFavoriteAppList),
            )
        }
    }

    private fun saveFavoriteAppList() {
        val currentFavoriteAppList = state.value.favoriteAppList
        val initialFavoriteAppList = state.value.initialFavoriteAppList

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

        updateState { copy(isSaveButtonEnabled = false) }

        viewModelScope.launch {
            try {
                appInfoRepository.updateAppInfoList(appsToUpdate)
                sendEffect(FavoriteAppSettingsEffect.ShowToast("保存しました"))
                sendEffect(FavoriteAppSettingsEffect.NavigateBack)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save favorite app settings", e)
                sendEffect(FavoriteAppSettingsEffect.ShowToast("保存に失敗しました"))
            }
        }
    }

    override fun onAction(action: FavoriteAppSettingsAction) {
        when (action) {
            is FavoriteAppSettingsAction.OnAllAppListAppClick -> addFavoriteAppList(action.appInfo)

            is FavoriteAppSettingsAction.OnFavoriteAppListAppClick -> removeFavoriteAppList(action.appInfo)

            is FavoriteAppSettingsAction.OnAppSearchQueryChange -> updateState { copy(appSearchQuery = action.query) }

            is FavoriteAppSettingsAction.OnSaveButtonClick -> saveFavoriteAppList()

            is FavoriteAppSettingsAction.OnBackButtonClick -> sendEffect(FavoriteAppSettingsEffect.NavigateBack)
        }
    }

    private fun List<AppInfo>.isSameAs(other: List<AppInfo>): Boolean {
        return this.size == other.size &&
            this.zip(other).all { (a, b) -> a.packageName == b.packageName }
    }

    private companion object {
        const val TimeoutMillis = 5000L

        const val TAG = "FavoriteAppSettingsViewModel"
    }
}
