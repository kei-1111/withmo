package com.example.withmo.ui.component.settingscreen.home

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat

@Composable
fun SettingNotification(
    showNotificationCheckDialog: Boolean,
    setShowNotificationCheckDialog: (Boolean) -> Unit,
    showNotificationAnimation: Boolean,
    setNotificationState: (Boolean) -> Unit
) {
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
                if (NotificationManagerCompat.getEnabledListenerPackages(context)
                        .contains(context.packageName).not()
                ) {
                    setNotificationState(false)
                } else {
                    setNotificationState(true)
                }
            }
        )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        UserSettingWithSwitch(
            title = "通知アニメーションを見る",
            checked = showNotificationAnimation,
            onCheckedChange = {
                if (NotificationManagerCompat.getEnabledListenerPackages(context)
                        .contains(context.packageName).not()
                ) {
                    setShowNotificationCheckDialog(true)
                }
                setNotificationState(!showNotificationAnimation)
            }
        )
    }

    if (showNotificationCheckDialog) {
        NotificationCheckDialog(
            onDismissRequest = {
                setNotificationState(false)
                setShowNotificationCheckDialog(false)
            },
            onConfirm = {
                launcher.launch(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                setShowNotificationCheckDialog(false)
            },
            onDismiss = {
                setNotificationState(false)
                setShowNotificationCheckDialog(false)
            }
        )
    }
}