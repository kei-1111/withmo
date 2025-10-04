package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
fun WithmoHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = WithmoTheme.colorScheme.outlineVariant,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

@Preview
@Composable
private fun WithmoHorizontalDividerLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoHorizontalDivider()
    }
}

@Preview
@Composable
private fun WithmoHorizontalDividerDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoHorizontalDivider()
    }
}
