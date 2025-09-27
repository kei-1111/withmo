package io.github.kei_1111.withmo.feature.setting.favorite_app

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteAppSettingsViewModel @Inject constructor(
    getFavoriteAppsUseCase: GetFavoriteAppsUseCase,
    getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
    private val saveFavoriteAppsUseCase: SaveFavoriteAppsUseCase,
) : StatefulBaseViewModel<FavoriteAppSettingsViewModelState, FavoriteAppSettingsState, FavoriteAppSettingsAction, FavoriteAppSettingsEffect>() {

    override fun createInitialViewModelState() = FavoriteAppSettingsViewModelState()
    override fun createInitialState() = FavoriteAppSettingsState.Idle

    private data class FavoriteAppSettingsData(
        val favoriteAppList: List<FavoriteAppInfo>,
        val initialFavoriteAppList: List<FavoriteAppInfo>,
        val appIconSettings: AppIconSettings,
    )
    private val favoriteAppSettingsDataStream = combine(
        getFavoriteAppsUseCase(),
        getAppIconSettingsUseCase(),
    ) { favoriteAppList, appIconSettings ->
        runCatching {
            FavoriteAppSettingsData(
                favoriteAppList = favoriteAppList.getOrThrow(),
                initialFavoriteAppList = favoriteAppList.getOrThrow(),
                appIconSettings = appIconSettings.getOrThrow(),
            )
        }
    }

    init {
        viewModelScope.launch {
            updateViewModelState { copy(statusType = FavoriteAppSettingsViewModelState.StatusType.LOADING) }
            favoriteAppSettingsDataStream.collect { result ->
                result
                    .onSuccess { data ->
                        updateViewModelState {
                            copy(
                                statusType = FavoriteAppSettingsViewModelState.StatusType.STABLE,
                                favoriteAppList = data.favoriteAppList.toPersistentList(),
                                initialFavoriteAppList = data.initialFavoriteAppList.toPersistentList(),
                                appIconSettings = data.appIconSettings,
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = FavoriteAppSettingsViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
                    }
            }
        }
    }

    override fun onAction(action: FavoriteAppSettingsAction) {
        when (action) {
            is FavoriteAppSettingsAction.OnAllAppListAppClick -> {
                val favoriteAppInfo = FavoriteAppInfo(
                    info = action.appInfo,
                    favoriteOrder = _viewModelState.value.favoriteAppList.size,
                )
                updateViewModelState {
                    if (favoriteAppList.size < AppConstants.FavoriteAppListMaxSize &&
                        favoriteAppList.none { it.info.packageName == action.appInfo.packageName }
                    ) {
                        copy(favoriteAppList = (favoriteAppList + favoriteAppInfo).toPersistentList())
                    } else {
                        this
                    }
                }
            }

            is FavoriteAppSettingsAction.OnFavoriteAppListAppClick -> {
                updateViewModelState {
                    copy(
                        favoriteAppList = favoriteAppList
                            .filterNot { it.info.packageName == action.appInfo.packageName }
                            .mapIndexed { index, favoriteApp -> favoriteApp.copy(favoriteOrder = index) }
                            .toPersistentList(),
                    )
                }
            }

            is FavoriteAppSettingsAction.OnAppSearchQueryChange -> {
                updateViewModelState { copy(appSearchQuery = action.query) }
            }

            is FavoriteAppSettingsAction.OnSaveButtonClick -> {
                viewModelScope.launch {
                    try {
                        saveFavoriteAppsUseCase(_viewModelState.value.favoriteAppList)
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
