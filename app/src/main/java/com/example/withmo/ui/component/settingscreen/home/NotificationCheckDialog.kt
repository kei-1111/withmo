package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.TitleLargeText

@Composable
fun NotificationCheckDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                },
            ) {
                BodyMediumText("設定へ")
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                },
            ) {
                BodyMediumText("キャンセル")
            }
        },
        title = {
            TitleLargeText("通知の設定")
        },
        text = {
            BodyMediumText("通知の設定を変更すると、通知が来たときにアニメーションをみることが出来ます。")
        },
    )
}
