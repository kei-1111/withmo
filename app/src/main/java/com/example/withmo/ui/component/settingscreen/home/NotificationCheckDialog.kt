package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun NotificationCheckDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("設定へ")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("キャンセル")
            }
        },
        title = {
            Text("通知の設定")
        },
        text = {
            Text("通知の設定を変更すると、通知が来たときにアニメーションをみることが出来ます。")
        },
//        shape = RoundedCornerShape(ROUNDED_CORNER_SHAPE)
    )

}