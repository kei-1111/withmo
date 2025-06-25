package io.github.kei_1111.withmo.feature.setting.favorite_app

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteApp
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteAppSettingsViewModel @Inject constructor(
    private val favoriteAppRepository: FavoriteAppRepository,
    private val getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
) : BaseViewModel<FavoriteAppSettingsState, FavoriteAppSettingsAction, FavoriteAppSettingsEffect>() {

    override fun createInitialState(): FavoriteAppSettingsState = FavoriteAppSettingsState()

    init {
        observeFavoriteAppList()
        observeAppIconSettings()
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            favoriteAppRepository.favoriteApps.collect { favoriteAppList ->
                val immutableFavoriteAppList = favoriteAppList.toPersistentList()

                updateState {
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
                updateState { copy(appIconSettings = appIconSettings) }
            }
        }
    }

    private fun addFavoriteAppList(appInfo: AppInfo) {
        val favoriteApp = FavoriteApp(
            info = appInfo,
            favoriteOrder = state.value.favoriteAppList.size,
        )
        val addedFavoriteAppList = (state.value.favoriteAppList + favoriteApp).toPersistentList()

        updateState {
            if (favoriteAppList.size < AppConstants.FavoriteAppListMaxSize &&
                favoriteAppList.none { it.info.packageName == appInfo.packageName }
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
            .filterNot { it.info.packageName == appInfo.packageName }
            .mapIndexed { index, favoriteApp -> favoriteApp.copy(favoriteOrder = index) }
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

        updateState { copy(isSaveButtonEnabled = false) }

        viewModelScope.launch {
            try {
                favoriteAppRepository.updateFavoriteApps(currentFavoriteAppList)
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

    private fun List<FavoriteApp>.isSameAs(other: List<FavoriteApp>): Boolean {
        return this.size == other.size &&
            this.zip(other).all { (a, b) -> a.info.packageName == b.info.packageName }
    }

    private companion object {
        const val TAG = "FavoriteAppSettingsViewModel"
    }
}
