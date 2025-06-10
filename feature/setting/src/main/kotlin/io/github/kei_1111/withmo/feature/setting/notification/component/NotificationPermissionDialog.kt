package io.github.kei_1111.withmo.feature.setting.notification.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.TitleLargeText
import io.github.kei_1111.withmo.feature.setting.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.SettingLightPreviewEnvironment

@Composable
internal fun NotificationPermissionDialog(
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
            BodyMediumText("この設定をオンにすると、通知が来たときにアニメーションをみることが出来ます。")
        },
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun NotificationPermissionDialogLightPreview() {
    SettingLightPreviewEnvironment {
        NotificationPermissionDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun NotificationPermissionDialogDarkPreview() {
    SettingDarkPreviewEnvironment {
        NotificationPermissionDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
