package io.github.kei_1111.withmo.feature.setting.favorite_app

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteAppSettingsViewModel @Inject constructor(
    private val getFavoriteAppsUseCase: GetFavoriteAppsUseCase,
    private val saveFavoriteAppsUseCase: SaveFavoriteAppsUseCase,
    private val getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
) : BaseViewModel<FavoriteAppSettingsViewModelState, FavoriteAppSettingsState, FavoriteAppSettingsAction, FavoriteAppSettingsEffect>() {

    override fun createInitialViewModelState() = FavoriteAppSettingsViewModelState()
    override fun createInitialState() = FavoriteAppSettingsState()

    init {
        observeFavoriteAppList()
        observeAppIconSettings()
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            getFavoriteAppsUseCase().collect { favoriteAppList ->
                val immutableFavoriteAppList = favoriteAppList.toPersistentList()

                updateViewModelState {
                    copy(
                        favoriteAppList = immutableFavoriteAppList,
                        initialFavoriteAppList = immutableFavoriteAppList,
                    )
                }
            }
        }
    }

    private fun observeAppIconSettings() {
        viewModelScope.launch {
            getAppIconSettingsUseCase().collect { appIconSettings ->
                updateViewModelState { copy(appIconSettings = appIconSettings) }
            }
        }
    }

    override fun onAction(action: FavoriteAppSettingsAction) {
        when (action) {
            is FavoriteAppSettingsAction.OnAllAppListAppClick -> {
                val favoriteAppInfo = FavoriteAppInfo(
                    info = action.appInfo,
                    favoriteOrder = state.value.favoriteAppList.size,
                )
                val addedFavoriteAppList = (state.value.favoriteAppList + favoriteAppInfo).toPersistentList()

                updateViewModelState {
                    if (favoriteAppList.size < AppConstants.FavoriteAppListMaxSize &&
                        favoriteAppList.none { it.info.packageName == action.appInfo.packageName }
                    ) {
                        copy(favoriteAppList = addedFavoriteAppList)
                    } else {
                        this
                    }
                }
            }

            is FavoriteAppSettingsAction.OnFavoriteAppListAppClick -> {
                val removedFavoriteAppList = state.value.favoriteAppList
                    .filterNot { it.info.packageName == action.appInfo.packageName }
                    .mapIndexed { index, favoriteApp -> favoriteApp.copy(favoriteOrder = index) }
                    .toPersistentList()

                updateViewModelState { copy(favoriteAppList = removedFavoriteAppList) }
            }

            is FavoriteAppSettingsAction.OnAppSearchQueryChange -> {
                updateViewModelState { copy(appSearchQuery = action.query) }
            }

            is FavoriteAppSettingsAction.OnSaveButtonClick -> {
                viewModelScope.launch {
                    try {
                        saveFavoriteAppsUseCase(state.value.favoriteAppList)
                        sendEffect(FavoriteAppSettingsEffect.ShowToast("保存しました"))
                        sendEffect(FavoriteAppSettingsEffect.NavigateBack)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to save favorite app settings", e)
                        sendEffect(FavoriteAppSettingsEffect.ShowToast("保存に失敗しました"))
                    }
                }
            }

            is FavoriteAppSettingsAction.OnBackButtonClick -> {
                sendEffect(FavoriteAppSettingsEffect.NavigateBack)
            }
        }
    }

    private companion object {
        const val TAG = "FavoriteAppSettingsViewModel"
    }
}
