package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun FileAccessCheckDialog(
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
            Text("ファイルの設定")
        },
        text = {
            Text("モデルを変更させるためには、ファイルアクセス権が必要です。")
        },
    )

}