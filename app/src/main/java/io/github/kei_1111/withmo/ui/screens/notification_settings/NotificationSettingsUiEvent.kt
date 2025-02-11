package io.github.kei_1111.withmo.ui.screens.notification_settings

import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface NotificationSettingsUiEvent : UiEvent {
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
