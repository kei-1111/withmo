package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.TitleLargeText

@Composable
fun FileAccessCheckDialog(
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
            TitleLargeText("ファイルアクセス権限")
        },
        text = {
            BodyMediumText("モデルを変更させるためには、ファイルアクセス権が必要です。")
        },
    )
}
