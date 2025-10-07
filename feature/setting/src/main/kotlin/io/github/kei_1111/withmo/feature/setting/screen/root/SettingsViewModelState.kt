package io.github.kei_1111.withmo.feature.setting.screen.root

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState

internal data class SettingsViewModelState(
    val statusType: StatusType = StatusType.STABLE,
    val isDefaultHomeApp: Boolean = true,
    val isNotificationPermissionDialogVisible: Boolean = false,
) : ViewModelState<SettingsState> {

    enum class StatusType { STABLE }

    override fun toState() = when (statusType) {
        StatusType.STABLE -> SettingsState.Stable(
            isDefaultHomeApp = isDefaultHomeApp,
            isNotificationPermissionDialogVisible = isNotificationPermissionDialogVisible,
        )
    }
}
