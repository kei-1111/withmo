package io.github.kei_1111.withmo.feature.setting.sort

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings

data class SortSettingsViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val sortSettings: SortSettings = SortSettings(),
    val initialSortSettings: SortSettings = SortSettings(),
    val isUsageStatsPermissionDialogVisible: Boolean = false,
) : ViewModelState<SortSettingsState> {

    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> SortSettingsState.Idle

        StatusType.LOADING -> SortSettingsState.Loading

        StatusType.STABLE -> SortSettingsState.Stable(
            sortSettings = sortSettings,
            isSaveButtonEnabled = sortSettings != initialSortSettings,
            isUsageStatsPermissionDialogVisible = isUsageStatsPermissionDialogVisible,
        )

        StatusType.ERROR -> SortSettingsState.Error(Throwable("An error occurred in SortSettingsViewModelState"))
    }
}
