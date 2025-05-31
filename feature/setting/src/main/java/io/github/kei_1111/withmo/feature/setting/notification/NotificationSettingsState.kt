package io.github.kei_1111.withmo.feature.setting.notification

import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings

data class NotificationSettingsState(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val initialNotificationSettings: NotificationSettings = NotificationSettings(),
    val isNotificationPermissionDialogShown: Boolean = false,
    val isSaveButtonEnabled: Boolean = false,
) : State
