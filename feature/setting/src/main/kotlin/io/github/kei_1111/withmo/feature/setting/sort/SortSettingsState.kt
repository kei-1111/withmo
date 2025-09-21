package io.github.kei_1111.withmo.feature.setting.sort

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings

data class SortSettingsState(
    val sortSettings: SortSettings = SortSettings(),
    val isSaveButtonEnabled: Boolean = false,
    val isUsageStatsPermissionDialogVisible: Boolean = false,
) : State
