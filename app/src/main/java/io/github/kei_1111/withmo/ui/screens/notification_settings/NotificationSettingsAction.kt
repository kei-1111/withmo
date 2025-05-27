package io.github.kei_1111.withmo.ui.screens.notification_settings

import io.github.kei_1111.withmo.core.featurebase.Action

sealed interface NotificationSettingsAction : Action {
    data class OnIsNotificationAnimationEnabledSwitchChange(val isNotificationAnimationEnabled: Boolean) : NotificationSettingsAction
    data object OnNotificationPermissionDialogConfirm : NotificationSettingsAction
    data object OnNotificationPermissionDialogDismiss : NotificationSettingsAction
    data object OnSaveButtonClick : NotificationSettingsAction
    data object OnBackButtonClick : NotificationSettingsAction

    data object OnNotificationListenerPermissionResult : NotificationSettingsAction
}
