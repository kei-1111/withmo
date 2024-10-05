package com.example.withmo.service

import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import com.example.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCase
import com.unity3d.player.UnityPlayer.UnitySendMessage
import dagger.hilt.android.AndroidEntryPoint
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
                val intent = Intent("notification_received")
                intent.putExtra("notification_data", sbn.packageName)
                sendBroadcast(intent)
                UnitySendMessage("Notification", "ShowObject", "")
            }
        }
    }
}
