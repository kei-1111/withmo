package io.github.kei_1111.withmo.feature.setting.screen.notification

import io.github.kei_1111.withmo.core.featurebase.Action

internal sealed interface NotificationSettingsAction : Action {
    data class OnIsNotificationAnimationEnabledSwitchChange(val isNotificationAnimationEnabled: Boolean) :
        NotificationSettingsAction
    data class OnIsNotificationBadgeEnabledSwitchChange(val isNotificationBadgeEnabled: Boolean) :
        NotificationSettingsAction
    data object OnSaveButtonClick : NotificationSettingsAction
    data object OnBackButtonClick : NotificationSettingsAction
    data object OnCheckPermissionOnResume : NotificationSettingsAction
}
