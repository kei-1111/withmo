package io.github.kei_1111.withmo.feature.setting.root

import io.github.kei_1111.withmo.core.featurebase.ViewModelState

data class SettingsViewModelState(
    val isDefaultHomeApp: Boolean = true,
    val isNotificationPermissionDialogVisible: Boolean = false,
) : ViewModelState<SettingsState> {
    override fun toState(): SettingsState = SettingsState(
        isDefaultHomeApp = isDefaultHomeApp,
        isNotificationPermissionDialogVisible = isNotificationPermissionDialogVisible,
    )
}
