package io.github.kei_1111.withmo.service

import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import io.github.kei_1111.withmo.common.IntentConstants
import io.github.kei_1111.withmo.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.common.unity.UnityMethod
import io.github.kei_1111.withmo.common.unity.UnityObject
import io.github.kei_1111.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {
    @Inject
    lateinit var getNotificationSettingsUseCase: GetNotificationSettingsUseCase

    private val serviceScope = CoroutineScope(Dispatchers.Default)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return
        serviceScope.launch {
            val notificationSettings = getNotificationSettingsUseCase().first()

            if (notificationSettings.isNotificationAnimationEnabled) {
                val intent = Intent(IntentConstants.Action.NotificationReceived)
                intent.putExtra(IntentConstants.ExtraKey.PackageName, sbn.packageName)
                sendBroadcast(intent)
                AndroidToUnityMessenger.sendMessage(UnityObject.Notification, UnityMethod.ShowObject, "")
            }
        }
    }
}
