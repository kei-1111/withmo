package com.example.withmo.domain.model

import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import com.example.withmo.domain.repository.UserSettingRepository
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
    lateinit var userSettingRepository: UserSettingRepository

    private val serviceScope = CoroutineScope(Dispatchers.Default)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return
        serviceScope.launch {
            val userSetting = userSettingRepository.userSetting.first()

            if (userSetting.showNotificationAnimation) {
                val intent = Intent("notification_received")
                intent.putExtra("notification_data", sbn.packageName)
                sendBroadcast(intent)
                UnitySendMessage("Notification", "ShowObject", "")
            }
        }
    }
}
