package io.github.kei_1111.withmo.feature.setting.screens.notification

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings

sealed interface NotificationSettingsState : State {
    data object Idle : NotificationSettingsState

    data object Loading : NotificationSettingsState

    data class Stable(
        val notificationSettings: NotificationSettings = NotificationSettings(),
        val isSaveButtonEnabled: Boolean = false,
    ) : NotificationSettingsState

    data class Error(val error: Throwable) : NotificationSettingsState
}
