package io.github.kei_1111.withmo.ui.screens.notification_settings

import io.github.kei_1111.withmo.domain.model.user_settings.NotificationSettings
import io.github.kei_1111.withmo.ui.base.UiState

data class NotificationSettingsUiState(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val initialNotificationSettings: NotificationSettings = NotificationSettings(),
    val isNotificationPermissionDialogShown: Boolean = false,
    val isSaveButtonEnabled: Boolean = false,
) : UiState
