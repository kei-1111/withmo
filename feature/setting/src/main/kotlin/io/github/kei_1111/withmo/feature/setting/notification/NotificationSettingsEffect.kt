package io.github.kei_1111.withmo.feature.setting.notification

import android.content.Intent
import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface NotificationSettingsEffect : Effect {
    data class RequestNotificationListenerPermission(val intent: Intent) :
        NotificationSettingsEffect
    data object NavigateBack : NotificationSettingsEffect
    data class ShowToast(val message: String) : NotificationSettingsEffect
}
