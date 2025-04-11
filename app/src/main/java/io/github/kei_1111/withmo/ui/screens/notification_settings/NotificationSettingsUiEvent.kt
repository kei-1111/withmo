package io.github.kei_1111.withmo.ui.screens.notification_settings

import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface NotificationSettingsUiEvent : UiEvent {
    data class OnIsNotificationAnimationEnabledSwitchChange(val isNotificationAnimationEnabled: Boolean) : NotificationSettingsUiEvent
    data object OnNotificationPermissionDialogConfirm : NotificationSettingsUiEvent
    data object OnNotificationPermissionDialogDismiss : NotificationSettingsUiEvent
    data object OnSaveButtonClick : NotificationSettingsUiEvent
    data object OnBackButtonClick : NotificationSettingsUiEvent
}
