package io.github.kei_1111.withmo.feature.setting.screens.sort.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.WithmoDialog
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
internal fun UsagePermissionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoDialog(
        title = "使用統計の権限が必要です",
        description = "使用回数順でソートするには、端末の使用統計にアクセスする権限が必要です。設定画面を開きますか？",
        dismissButtonText = "キャンセル",
        confirmButtonText = "設定を開く",
        onDismissRequest = onDismiss,
        onDismissClick = onDismiss,
        onConfirmClick = onConfirm,
        modifier = modifier,
    )
}

@Composable
@Preview
private fun UsagePermissionDialogLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        UsagePermissionDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Composable
@Preview
private fun UsagePermissionDialogDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        UsagePermissionDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
