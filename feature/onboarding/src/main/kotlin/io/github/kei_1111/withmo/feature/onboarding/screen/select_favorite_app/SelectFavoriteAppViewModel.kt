package io.github.kei_1111.withmo.feature.onboarding.screen.select_favorite_app

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SelectFavoriteAppViewModel @Inject constructor(
    getFavoriteAppsUseCase: GetFavoriteAppsUseCase,
    private val saveFavoriteAppsUseCase: SaveFavoriteAppsUseCase,
) : StatefulBaseViewModel<SelectFavoriteAppViewModelState, SelectFavoriteAppState, SelectFavoriteAppAction, SelectFavoriteAppEffect>() {

    override fun createInitialViewModelState() = SelectFavoriteAppViewModelState()
    override fun createInitialState() = SelectFavoriteAppState.Idle

    private val selectFavoriteAppDataStream: Flow<Result<List<FavoriteAppInfo>>> = getFavoriteAppsUseCase()

    init {
        viewModelScope.launch {
            updateViewModelState { copy(statusType = SelectFavoriteAppViewModelState.StatusType.LOADING) }
            selectFavoriteAppDataStream.collect { result ->
                result
                    .onSuccess { data ->
                        updateViewModelState {
                            copy(
                                statusType = SelectFavoriteAppViewModelState.StatusType.STABLE,
                                selectedAppList = data.toPersistentList(),
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = SelectFavoriteAppViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
                    }
            }
        }
    }

    private fun saveFavoriteApps() {
        viewModelScope.launch {
            try {
                saveFavoriteAppsUseCase(_viewModelState.value.selectedAppList)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save favorite app settings", e)
            }
        }
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    override fun onAction(action: SelectFavoriteAppAction) {
        when (action) {
            is SelectFavoriteAppAction.OnAllAppListAppClick -> {
                val favoriteAppInfo = FavoriteAppInfo(
                    info = action.appInfo,
                    favoriteOrder = _viewModelState.value.selectedAppList.size,
                )
                updateViewModelState {
                    if (selectedAppList.size < AppConstants.FAVORITE_APP_LIST_MAX_SIZE &&
                        selectedAppList.none { it.info.packageName == favoriteAppInfo.info.packageName }
                    ) {
                        copy(selectedAppList = (selectedAppList + favoriteAppInfo).toPersistentList())
                    } else {
                        this
                    }
                }
            }

            is SelectFavoriteAppAction.OnFavoriteAppListAppClick -> {
                updateViewModelState {
                    copy(
                        selectedAppList = selectedAppList
                            .filterNot { it.info.packageName == action.appInfo.packageName }
                            .mapIndexed { index, favoriteApp -> favoriteApp.copy(favoriteOrder = index) }
                            .toPersistentList(),
                    )
                }
            }

            is SelectFavoriteAppAction.OnAppSearchQueryChange -> {
                updateViewModelState { copy(appSearchQuery = action.query) }
            }

            is SelectFavoriteAppAction.OnBackButtonClick -> {
                saveFavoriteApps()
                sendEffect(SelectFavoriteAppEffect.NavigateBack)
            }

            is SelectFavoriteAppAction.OnNextButtonClick -> {
                saveFavoriteApps()
                sendEffect(SelectFavoriteAppEffect.NavigateSelectDisplayModel)
            }
        }
    }

    private companion object {
        const val TAG = "SelectFavoriteAppViewModel"
    }
}
