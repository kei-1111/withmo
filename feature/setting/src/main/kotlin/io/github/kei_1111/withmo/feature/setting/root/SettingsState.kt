package io.github.kei_1111.withmo.feature.setting.root

import io.github.kei_1111.withmo.core.featurebase.State

data class SettingsState(
    val isDefaultHomeApp: Boolean = true,
    val isNotificationPermissionDialogVisible: Boolean = false,
) : State
