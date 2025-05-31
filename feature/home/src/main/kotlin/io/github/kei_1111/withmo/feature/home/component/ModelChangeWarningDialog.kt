package io.github.kei_1111.withmo.feature.home.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.TitleLargeText

@Composable
internal fun ModelChangeWarningDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                },
            ) {
                BodyMediumText("確認した")
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
            TitleLargeText("モデル変更の注意事項")
        },
        text = {
            BodyMediumText("表示モデルは、VRMファイルのみ対応しております。表示したいモデルのVRMファイルを選択してください。")
        },
    )
}
