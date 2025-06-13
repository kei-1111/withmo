package io.github.kei_1111.withmo.feature.setting.favorite_app

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetSortSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.FavoriteOrder
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
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

    private val currentAppList = mutableListOf<WithmoAppInfo>()

    override fun createInitialState(): FavoriteAppSettingsState = FavoriteAppSettingsState()

    init {
        observeAppList()
        observeFavoriteAppList()
        observeAppIconSettings()
    }

    private fun observeAppList() {
        viewModelScope.launch {
            appInfoRepository.getAllList().collect { withmoAppList ->
                currentAppList.clear()
                currentAppList.addAll(withmoAppList)
                val filteredAppList = filterAppList(state.value.appSearchQuery, withmoAppList).toPersistentList()
                updateState { copy(searchedAppList = filteredAppList) }
            }
        }
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            appInfoRepository.getFavoriteList().collect { favoriteAppList ->
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

    private suspend fun filterAppList(query: String, appList: List<WithmoAppInfo>): List<WithmoAppInfo> =
        withContext(Dispatchers.Default) {
            val sortType = getSortSettingsUseCase().first().sortType
            val filteredAppList = appList.filter { appInfo ->
                appInfo.info.label.contains(query, ignoreCase = true)
            }
            sortAppList(sortType, filteredAppList)
        }

    private fun addFavoriteAppList(withmoAppInfo: WithmoAppInfo) {
        val addedFavoriteAppList = (state.value.favoriteAppList + withmoAppInfo).toPersistentList()

        updateState {
            if (favoriteAppList.size < AppConstants.FavoriteAppListMaxSize &&
                favoriteAppList.none { it.info.packageName == withmoAppInfo.info.packageName }
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

    private fun removeFavoriteAppList(withmoAppInfo: WithmoAppInfo) {
        val removedFavoriteAppList = state.value.favoriteAppList
            .filterNot { it.info.packageName == withmoAppInfo.info.packageName }
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

        val currentPackageNames = currentFavoriteAppList.map { it.info.packageName }.toSet()

        val appsToRemoveFromFavorites = initialFavoriteAppList.filter { withmoAppInfo ->
            withmoAppInfo.favoriteOrder != FavoriteOrder.NotFavorite && !currentPackageNames.contains(withmoAppInfo.info.packageName)
        }.map { appInfo ->
            appInfo.copy(favoriteOrder = FavoriteOrder.NotFavorite)
        }

        val appsToUpdateFavorites = currentFavoriteAppList.mapIndexed { index, withmoAppInfo ->
            withmoAppInfo.copy(
                favoriteOrder = favoriteOrders.getOrNull(index) ?: FavoriteOrder.NotFavorite,
            )
        }

        val appsToUpdate = appsToUpdateFavorites + appsToRemoveFromFavorites

        updateState { copy(isSaveButtonEnabled = false) }

        viewModelScope.launch {
            try {
                appInfoRepository.updateList(appsToUpdate)
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
            is FavoriteAppSettingsAction.OnAllAppListAppClick -> addFavoriteAppList(action.withmoAppInfo)

            is FavoriteAppSettingsAction.OnFavoriteAppListAppClick -> removeFavoriteAppList(action.withmoAppInfo)

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

    private fun List<WithmoAppInfo>.isSameAs(other: List<WithmoAppInfo>): Boolean {
        return this.size == other.size &&
            this.zip(other).all { (a, b) -> a.info.packageName == b.info.packageName }
    }

    private companion object {
        const val TimeoutMillis = 5000L

        const val TAG = "FavoriteAppSettingsViewModel"
    }
}
