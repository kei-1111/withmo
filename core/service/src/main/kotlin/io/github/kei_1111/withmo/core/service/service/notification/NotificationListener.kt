package io.github.kei_1111.withmo.core.service.service.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import io.github.kei_1111.withmo.core.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.core.common.unity.UnityMethod
import io.github.kei_1111.withmo.core.common.unity.UnityObject
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.domain.usecase.GetNotificationSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {
    @Inject
    lateinit var getNotificationSettingsUseCase: GetNotificationSettingsUseCase

    @Inject
    lateinit var appManager: AppManager

    private val serviceScope = CoroutineScope(Dispatchers.Default)

    /**
     * 現在の通知欄にある通知を取得し、アプリごとに通知があるかを判定
     * フォアグラウンドサービスやメディア再生などの常駐通知は除外
     * @return パッケージ名をキー、通知の有無をバリューとするMap
     */
    private fun getCurrentNotifications(): Map<String, Boolean> {
        return try {
            val notifications = super.getActiveNotifications() ?: return emptyMap()
            val notificationMap = mutableMapOf<String, Boolean>()

            notifications.forEach { sbn ->
                val packageName = sbn.packageName
                if (packageName != this.packageName && isUserNotification(sbn)) {
                    notificationMap[packageName] = true
                }
            }

            notificationMap
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get active notifications", e)
            emptyMap()
        }
    }

    /**
     * ユーザー通知かどうかを判定（フォアグラウンドサービスやメディア通知を除外）
     */
    private fun isUserNotification(sbn: StatusBarNotification): Boolean {
        val notification = sbn.notification ?: return false

        return when {
            (notification.flags and Notification.FLAG_FOREGROUND_SERVICE) != 0 -> false
            (notification.flags and Notification.FLAG_ONGOING_EVENT) != 0 -> false
            (notification.flags and Notification.FLAG_NO_CLEAR) != 0 -> false
            sbn.packageName == "android" || sbn.packageName == "com.android.systemui" -> false
            else -> true
        }
    }

    private fun updateNotificationFlags() {
        val currentNotifications = getCurrentNotifications()
        appManager.updateNotifications(currentNotifications)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        serviceScope.launch {
            updateNotificationFlags()
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return
        serviceScope.launch {
            val result = getNotificationSettingsUseCase().first()

            result
                .onSuccess { notificationSettings ->
                    if (notificationSettings.isNotificationAnimationEnabled) {
                        AndroidToUnityMessenger.sendMessage(UnityObject.Notification, UnityMethod.ShowObject, "")
                    }

                    if (notificationSettings.isNotificationBadgeEnabled) {
                        updateNotificationFlags()
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "Failed to get notification settings", error)
                }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        if (sbn == null) return
        serviceScope.launch {
            val result = getNotificationSettingsUseCase().first()

            result
                .onSuccess { notificationSettings ->
                    if (notificationSettings.isNotificationBadgeEnabled) {
                        updateNotificationFlags()
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "Failed to get notification settings", error)
                }
        }
    }

    private companion object {
        const val TAG = "NotificationListener"
    }
}
