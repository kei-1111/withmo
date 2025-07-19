package io.github.kei_1111.withmo.feature.setting.favorite_app

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteAppSettingsViewModel @Inject constructor(
    private val getFavoriteAppsUseCase: GetFavoriteAppsUseCase,
    private val saveFavoriteAppsUseCase: SaveFavoriteAppsUseCase,
    private val getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
) : BaseViewModel<FavoriteAppSettingsState, FavoriteAppSettingsAction, FavoriteAppSettingsEffect>() {

    override fun createInitialState(): FavoriteAppSettingsState = FavoriteAppSettingsState()

    init {
        observeFavoriteAppList()
        observeAppIconSettings()
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            getFavoriteAppsUseCase().collect { favoriteAppList ->
                val immutableFavoriteAppList = favoriteAppList.toPersistentList()

                updateState {
                    copy(
                        favoriteAppInfoList = immutableFavoriteAppList,
                        initialFavoriteAppInfoList = immutableFavoriteAppList,
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
        val favoriteAppInfo = FavoriteAppInfo(
            info = appInfo,
            favoriteOrder = state.value.favoriteAppInfoList.size,
        )
        val addedFavoriteAppList = (state.value.favoriteAppInfoList + favoriteAppInfo).toPersistentList()

        updateState {
            if (favoriteAppInfoList.size < AppConstants.FavoriteAppListMaxSize &&
                favoriteAppInfoList.none { it.info.packageName == appInfo.packageName }
            ) {
                copy(
                    favoriteAppInfoList = addedFavoriteAppList,
                    isSaveButtonEnabled = !addedFavoriteAppList.isSameAs(initialFavoriteAppInfoList),
                )
            } else {
                this
            }
        }
    }

    private fun removeFavoriteAppList(appInfo: AppInfo) {
        val removedFavoriteAppList = state.value.favoriteAppInfoList
            .filterNot { it.info.packageName == appInfo.packageName }
            .mapIndexed { index, favoriteApp -> favoriteApp.copy(favoriteOrder = index) }
            .toPersistentList()

        updateState {
            copy(
                favoriteAppInfoList = removedFavoriteAppList,
                isSaveButtonEnabled = (initialFavoriteAppInfoList != removedFavoriteAppList),
            )
        }
    }

    private fun saveFavoriteAppList() {
        val currentFavoriteAppList = state.value.favoriteAppInfoList

        updateState { copy(isSaveButtonEnabled = false) }

        viewModelScope.launch {
            try {
                saveFavoriteAppsUseCase(currentFavoriteAppList)
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

    private fun List<FavoriteAppInfo>.isSameAs(other: List<FavoriteAppInfo>): Boolean {
        return this.size == other.size &&
            this.zip(other).all { (a, b) -> a.info.packageName == b.info.packageName }
    }

    private companion object {
        const val TAG = "FavoriteAppSettingsViewModel"
    }
}
