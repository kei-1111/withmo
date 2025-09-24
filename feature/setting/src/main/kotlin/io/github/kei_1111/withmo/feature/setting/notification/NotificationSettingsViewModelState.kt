package io.github.kei_1111.withmo.feature.setting.notification

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings

data class NotificationSettingsViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val initialNotificationSettings: NotificationSettings = NotificationSettings(),
) : ViewModelState<NotificationSettingsState> {

    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> NotificationSettingsState.Idle

        StatusType.LOADING -> NotificationSettingsState.Loading

        StatusType.STABLE -> NotificationSettingsState.Stable(
            notificationSettings = notificationSettings,
            isSaveButtonEnabled = notificationSettings != initialNotificationSettings,
        )

        StatusType.ERROR -> NotificationSettingsState.Error(Throwable("An error occurred in NotificationSettingsViewModelState"))
    }
}
