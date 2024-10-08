package com.example.withmo.ui.screens.notification_settings

import com.example.withmo.domain.model.user_settings.NotificationSettings
import com.example.withmo.ui.base.UiState

data class NotificationSettingsUiState(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val initialNotificationSettings: NotificationSettings = NotificationSettings(),
    val isNotificationPermissionDialogShown: Boolean = false,
    val isSaveButtonEnabled: Boolean = false,
) : UiState
