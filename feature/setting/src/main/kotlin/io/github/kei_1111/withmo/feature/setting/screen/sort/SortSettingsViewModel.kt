package io.github.kei_1111.withmo.feature.setting.screen.sort

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.domain.usecase.GetSortSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSortSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SortSettingsViewModel @Inject constructor(
    getSortSettingsUseCase: GetSortSettingsUseCase,
    private val saveSortSettingsUseCase: SaveSortSettingsUseCase,
    private val appManager: AppManager,
    private val permissionChecker: PermissionChecker,
) : StatefulBaseViewModel<SortSettingsViewModelState, SortSettingsState, SortSettingsAction, SortSettingsEffect>() {

    override fun createInitialViewModelState() = SortSettingsViewModelState()
    override fun createInitialState() = SortSettingsState.Idle

    private val sortSettingsDataStream = getSortSettingsUseCase()

    init {
        viewModelScope.launch {
            updateViewModelState { copy(statusType = SortSettingsViewModelState.StatusType.LOADING) }
            sortSettingsDataStream.collect { result ->
                result
                    .onSuccess { sortSettings ->
                        updateViewModelState {
                            copy(
                                statusType = SortSettingsViewModelState.StatusType.STABLE,
                                sortSettings = sortSettings,
                                initialSortSettings = sortSettings,
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = SortSettingsViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
                    }
            }
        }
    }

    private fun updateSortTypeToUseCount() {
        updateViewModelState {
            val updatedSortSettings = sortSettings.copy(sortType = SortType.USE_COUNT)
            copy(sortSettings = updatedSortSettings)
        }
        viewModelScope.launch {
            try {
                appManager.updateUsageCounts()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update usage counts", e)
            }
        }
    }

    override fun onAction(action: SortSettingsAction) {
        when (action) {
            is SortSettingsAction.OnSortTypeRadioButtonClick -> {
                if (action.sortType == SortType.USE_COUNT) {
                    if (!permissionChecker.isUsageStatsPermissionGranted()) {
                        updateViewModelState { copy(isUsageStatsPermissionDialogVisible = true) }
                    } else {
                        updateSortTypeToUseCount()
                    }
                } else {
                    updateViewModelState {
                        val updatedSortSettings = sortSettings.copy(sortType = action.sortType)
                        copy(sortSettings = updatedSortSettings)
                    }
                }
            }

            is SortSettingsAction.OnSaveButtonClick -> {
                viewModelScope.launch {
                    try {
                        saveSortSettingsUseCase(_viewModelState.value.sortSettings)
                        sendEffect(SortSettingsEffect.ShowToast("保存しました"))
                        sendEffect(SortSettingsEffect.NavigateBack)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to save sort settings", e)
                        sendEffect(SortSettingsEffect.ShowToast("保存に失敗しました"))
                    }
                }
            }

            is SortSettingsAction.OnBackButtonClick -> {
                sendEffect(SortSettingsEffect.NavigateBack)
            }

            is SortSettingsAction.OnUsageStatsPermissionDialogConfirm -> {
                updateViewModelState { copy(isUsageStatsPermissionDialogVisible = false) }
                sendEffect(SortSettingsEffect.RequestUsageStatsPermission)
            }

            is SortSettingsAction.OnUsageStatsPermissionDialogDismiss -> {
                updateViewModelState { copy(isUsageStatsPermissionDialogVisible = false) }
            }

            is SortSettingsAction.OnUsageStatsPermissionResult -> {
                if (permissionChecker.isUsageStatsPermissionGranted()) {
                    updateSortTypeToUseCount()
                    sendEffect(SortSettingsEffect.ShowToast("使用回数取得の権限が許可されました"))
                } else {
                    sendEffect(SortSettingsEffect.ShowToast("使用回数取得の権限が必要です"))
                }
            }
        }
    }

    private companion object {
        const val TAG = "SortSettingsViewModel"
    }
}
