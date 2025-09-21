package io.github.kei_1111.withmo.feature.setting.notification

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings

data class NotificationSettingsViewModelState(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val initialNotificationSettings: NotificationSettings = NotificationSettings(),
) : ViewModelState<NotificationSettingsState> {
    override fun toState() = NotificationSettingsState(
        notificationSettings = notificationSettings,
        isSaveButtonEnabled = notificationSettings != initialNotificationSettings,
    )
}
