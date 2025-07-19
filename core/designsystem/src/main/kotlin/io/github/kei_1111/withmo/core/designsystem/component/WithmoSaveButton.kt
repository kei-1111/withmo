package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions

@Composable
fun WithmoSaveButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .height(CommonDimensions.SettingItemHeight),
        enabled = enabled,
    ) {
        BodyMediumText(
            text = "保存",
            color = if (enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled)
            },
        )
    }
}

@Preview
@Composable
private fun WithmoSaveButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoSaveButton(
            onClick = {},
            enabled = true,
        )
    }
}

@Preview
@Composable
private fun WithmoSaveButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoSaveButton(
            onClick = {},
            enabled = true,
        )
    }
}
