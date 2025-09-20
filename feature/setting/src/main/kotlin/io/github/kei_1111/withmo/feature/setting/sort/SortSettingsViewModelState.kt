package io.github.kei_1111.withmo.feature.setting.sort

import io.github.kei_1111.withmo.core.featurebase.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings

data class SortSettingsViewModelState(
    val sortSettings: SortSettings = SortSettings(),
    val initialSortSettings: SortSettings = SortSettings(),
    val isUsageStatsPermissionDialogVisible: Boolean = false,
) : ViewModelState<SortSettingsState> {
    override fun toState() = SortSettingsState(
        sortSettings = sortSettings,
        isSaveButtonEnabled = sortSettings != initialSortSettings,
        isUsageStatsPermissionDialogVisible = isUsageStatsPermissionDialogVisible,
    )
}
