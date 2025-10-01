package io.github.kei_1111.withmo.feature.setting.screens.sort

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings

internal sealed interface SortSettingsState : State {
    data object Idle : SortSettingsState

    data object Loading : SortSettingsState

    data class Stable(
        val sortSettings: SortSettings = SortSettings(),
        val isSaveButtonEnabled: Boolean = false,
        val isUsageStatsPermissionDialogVisible: Boolean = false,
    ) : SortSettingsState

    data class Error(val error: Throwable) : SortSettingsState
}
