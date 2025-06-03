package io.github.kei_1111.withmo.feature.setting.favorite_app

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetSortSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteOrder
import io.github.kei_1111.withmo.core.model.user_settings.sortAppList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteAppSettingsViewModel @Inject constructor(
    private val appInfoRepository: AppInfoRepository,
    private val getSortSettingsUseCase: GetSortSettingsUseCase,
    private val getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
) : BaseViewModel<FavoriteAppSettingsState, FavoriteAppSettingsAction, FavoriteAppSettingsEffect>() {

    private val currentAppList = mutableListOf<AppInfo>()

    override fun createInitialState(): FavoriteAppSettingsState = FavoriteAppSettingsState()

    init {
        observeAppList()
        observeFavoriteAppList()
        observeAppIconSettings()
    }

    private fun observeAppList() {
        viewModelScope.launch {
            appInfoRepository.getAllAppInfoList().collect { appList ->
                currentAppList.clear()
                currentAppList.addAll(appList)
                val filteredAppList = filterAppList(state.value.appSearchQuery, appList).toPersistentList()
                updateState { copy(searchedAppList = filteredAppList) }
            }
        }
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { favoriteAppList ->
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

    private suspend fun filterAppList(query: String, appList: List<AppInfo>): List<AppInfo> =
        withContext(Dispatchers.Default) {
            val sortType = getSortSettingsUseCase().first().sortType
            val filteredAppList = appList.filter { appInfo ->
                appInfo.label.contains(query, ignoreCase = true)
            }
            sortAppList(sortType, filteredAppList)
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

            is FavoriteAppSettingsAction.OnAppSearchQueryChange -> {
                updateState { copy(appSearchQuery = action.query) }
                viewModelScope.launch {
                    val filteredAppList = filterAppList(action.query, currentAppList)
                    updateState { copy(searchedAppList = filteredAppList.toPersistentList()) }
                }
            }

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
