package com.example.withmo.ui.screens.notification_settings

sealed interface NotificationSettingsUiEvent {
    data class ChangeIsNotificationAnimationEnabled(
        val isNotificationAnimationEnabled: Boolean,
    ) : NotificationSettingsUiEvent
    data object NotificationPermissionDialogOnDismiss : NotificationSettingsUiEvent
    data object NotificationPermissionDialogOnConfirm : NotificationSettingsUiEvent
    data object Save : NotificationSettingsUiEvent
    data object SaveSuccess : NotificationSettingsUiEvent
    data object SaveFailure : NotificationSettingsUiEvent
    data object NavigateToSettingsScreen : NotificationSettingsUiEvent
}
