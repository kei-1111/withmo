package io.github.kei_1111.withmo.feature.setting.sort

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.domain.usecase.GetSortSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSortSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SortSettingsViewModel @Inject constructor(
    private val getSortSettingsUseCase: GetSortSettingsUseCase,
    private val saveSortSettingsUseCase: SaveSortSettingsUseCase,
    private val permissionChecker: PermissionChecker,
) : BaseViewModel<SortSettingsState, SortSettingsAction, SortSettingsEffect>() {

    override fun createInitialState(): SortSettingsState = SortSettingsState()

    init {
        observerSortSettings()
    }

    private fun observerSortSettings() {
        viewModelScope.launch {
            getSortSettingsUseCase().collect { sortSettings ->
                updateState {
                    copy(
                        sortSettings = sortSettings,
                        initialSortSettings = sortSettings,
                    )
                }
            }
        }
    }

    private fun saveSortSettings() {
        updateState { copy(isSaveButtonEnabled = false) }
        viewModelScope.launch {
            try {
                val sortSettings = state.value.sortSettings
                saveSortSettingsUseCase(sortSettings)
                sendEffect(SortSettingsEffect.ShowToast("保存しました"))
                sendEffect(SortSettingsEffect.NavigateBack)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save sort settings", e)
                sendEffect(SortSettingsEffect.ShowToast("保存に失敗しました"))
            }
        }
    }

    override fun onAction(action: SortSettingsAction) {
        when (action) {
            is SortSettingsAction.OnSortTypeRadioButtonClick -> {
                handleSortTypeSelection(action.sortType)
            }

            is SortSettingsAction.OnSaveButtonClick -> saveSortSettings()

            is SortSettingsAction.OnBackButtonClick -> sendEffect(SortSettingsEffect.NavigateBack)

            is SortSettingsAction.OnUsageStatsPermissionDialogConfirm -> {
                updateState { copy(isUsageStatsPermissionDialogVisible = false) }
                sendEffect(SortSettingsEffect.RequestUsageStatsPermission)
            }

            is SortSettingsAction.OnUsageStatsPermissionDialogDismiss -> {
                updateState { copy(isUsageStatsPermissionDialogVisible = false) }
            }

            is SortSettingsAction.OnUsageStatsPermissionResult -> {
                checkUsageStatsPermissionAndUpdate()
            }
        }
    }

    private fun handleSortTypeSelection(sortType: SortType) {
        if (sortType == SortType.USE_COUNT && !permissionChecker.isUsageStatsPermissionGranted()) {
            updateState { copy(isUsageStatsPermissionDialogVisible = true) }
        } else {
            updateSortType(sortType)
        }
    }

    private fun updateSortType(sortType: SortType) {
        updateState {
            val updatedSortSettings = sortSettings.copy(sortType = sortType)
            copy(
                sortSettings = updatedSortSettings,
                isSaveButtonEnabled = updatedSortSettings != initialSortSettings,
            )
        }
    }

    private fun checkUsageStatsPermissionAndUpdate() {
        if (permissionChecker.isUsageStatsPermissionGranted()) {
            updateSortType(SortType.USE_COUNT)
            sendEffect(SortSettingsEffect.ShowToast("使用統計の権限が許可されました"))
        } else {
            sendEffect(SortSettingsEffect.ShowToast("使用統計の権限が必要です"))
        }
    }

    private companion object {
        const val TAG = "SortSettingsViewModel"
    }
}
