package io.github.kei_1111.withmo.feature.setting.screens.root

import io.github.kei_1111.withmo.core.featurebase.stateful.State

internal sealed interface SettingsState : State {
    data class Stable(
        val isDefaultHomeApp: Boolean = true,
        val isNotificationPermissionDialogVisible: Boolean = false,
    ) : SettingsState
}
