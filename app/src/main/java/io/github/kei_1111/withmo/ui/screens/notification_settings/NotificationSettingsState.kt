package io.github.kei_1111.withmo.ui.screens.notification_settings

import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.domain.model.user_settings.NotificationSettings

data class NotificationSettingsState(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val initialNotificationSettings: NotificationSettings = NotificationSettings(),
    val isNotificationPermissionDialogShown: Boolean = false,
    val isSaveButtonEnabled: Boolean = false,
) : State
