package io.github.kei_1111.withmo.feature.setting.screens.notification

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings

internal data class NotificationSettingsViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val initialNotificationSettings: NotificationSettings = NotificationSettings(),
    val error: Throwable? = null,
) : ViewModelState<NotificationSettingsState> {

    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> NotificationSettingsState.Idle

        StatusType.LOADING -> NotificationSettingsState.Loading

        StatusType.STABLE -> NotificationSettingsState.Stable(
            notificationSettings = notificationSettings,
            isSaveButtonEnabled = notificationSettings != initialNotificationSettings,
        )

        StatusType.ERROR -> NotificationSettingsState.Error(error ?: Throwable("Unknown error"))
    }
}
