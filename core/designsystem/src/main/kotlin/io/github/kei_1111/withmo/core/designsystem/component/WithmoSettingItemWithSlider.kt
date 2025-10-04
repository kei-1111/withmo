package io.github.kei_1111.withmo.core.designsystem.component

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
fun WithmoSettingItemWithSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @IntRange(from = 0.toLong()) steps: Int = 0,
) {
    Surface(
        modifier = modifier,
        color = WithmoTheme.colorScheme.surfaceContainer,
        shape = WithmoTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = title,
                color = WithmoTheme.colorScheme.onSurface,
                style = WithmoTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 16.dp),
            )
            WithmoSlider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                enabled = enabled,
                steps = steps,
            )
        }
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithSliderLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoSettingItemWithSlider(
            title = "設定項目",
            value = 0.5f,
            onValueChange = {},
            valueRange = 0f..1f,
        )
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithSliderDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoSettingItemWithSlider(
            title = "設定項目",
            value = 0.5f,
            onValueChange = {},
            valueRange = 0f..1f,
        )
    }
}
