package com.example.withmo.domain.model

import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import com.unity3d.player.UnityPlayer.UnitySendMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener() : NotificationListenerService() {
    @Inject
    lateinit var userSettingRepository: UserSettingRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return
        if (userSettingRepository.getUserSettingData().showNotificationAnimation) {
            val intent = Intent("notification_received")
            intent.putExtra("notification_data", sbn.packageName)
            sendBroadcast(intent)
            UnitySendMessage("Notification", "ShowObject", "")
        }
    }
}
