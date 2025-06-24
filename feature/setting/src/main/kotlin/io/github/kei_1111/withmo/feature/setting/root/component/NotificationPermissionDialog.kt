package io.github.kei_1111.withmo.feature.setting.root.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.WithmoDialog
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment

@Composable
internal fun NotificationPermissionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoDialog(
        title = "通知の設定",
        description = "この設定をオンにすると、通知が来たときにアニメーションをみることが出来ます。",
        dismissButtonText = "キャンセル",
        confirmButtonText = "設定へ",
        onDismissRequest = onDismiss,
        onDismissClick = onDismiss,
        onConfirmClick = onConfirm,
        modifier = modifier,
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
