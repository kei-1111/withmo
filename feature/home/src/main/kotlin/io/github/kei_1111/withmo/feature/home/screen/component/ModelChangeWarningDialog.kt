package io.github.kei_1111.withmo.feature.home.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.WithmoDialog
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
internal fun ModelChangeWarningDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoDialog(
        title = "モデル変更の注意事項",
        description = "表示モデルは、VRMファイルのみ対応しております。表示したいモデルのVRMファイルを選択してください。",
        dismissButtonText = "キャンセル",
        confirmButtonText = "確認した",
        onDismissRequest = onDismiss,
        onDismissClick = onDismiss,
        onConfirmClick = onConfirm,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun ModelChangeWarningDialogLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ModelChangeWarningDialog(
            onConfirm = { },
            onDismiss = { },
        )
    }
}

@Preview
@Composable
private fun ModelChangeWarningDialogDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ModelChangeWarningDialog(
            onConfirm = { },
            onDismiss = { },
        )
    }
}
