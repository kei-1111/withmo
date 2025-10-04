package io.github.kei_1111.withmo.feature.setting.screens.root.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.WithmoDialog
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

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

@Composable
@Preview
private fun NotificationPermissionDialogLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        NotificationPermissionDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Composable
@Preview
private fun NotificationPermissionDialogDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        NotificationPermissionDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
