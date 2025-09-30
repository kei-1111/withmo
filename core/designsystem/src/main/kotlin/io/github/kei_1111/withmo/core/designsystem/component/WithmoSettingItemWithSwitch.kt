package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

private const val SWITCH_SCALE = 0.75f

@Composable
fun WithmoSettingItemWithSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.height(56.dp),
        color = WithmoTheme.colorScheme.surfaceContainer,
        shape = WithmoTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = WithmoTheme.colorScheme.onSurface,
                style = WithmoTheme.typography.bodyMedium,
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.scale(SWITCH_SCALE),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = WithmoTheme.colorScheme.onPrimary,
                    checkedTrackColor = WithmoTheme.colorScheme.primary,
                    checkedBorderColor = Color.Transparent,
                    checkedIconColor = WithmoTheme.colorScheme.onPrimaryContainer,
                    uncheckedThumbColor = WithmoTheme.colorScheme.outline,
                    uncheckedTrackColor = WithmoTheme.colorScheme.surfaceContainerHighest,
                    uncheckedBorderColor = WithmoTheme.colorScheme.outline,
                    uncheckedIconColor = WithmoTheme.colorScheme.surfaceContainerHighest,
                    disabledCheckedThumbColor = WithmoTheme.colorScheme.surface
                        .compositeOver(WithmoTheme.colorScheme.surface),
                    disabledCheckedTrackColor = WithmoTheme.colorScheme.onSurface
                        .copy(alpha = 0.12f)
                        .compositeOver(WithmoTheme.colorScheme.surface),
                    disabledCheckedBorderColor = Color.Transparent,
                    disabledCheckedIconColor = WithmoTheme.colorScheme.onSurface
                        .copy(alpha = 0.38f)
                        .compositeOver(WithmoTheme.colorScheme.surface),
                    disabledUncheckedThumbColor = WithmoTheme.colorScheme.onSurface
                        .copy(alpha = 0.38f)
                        .compositeOver(WithmoTheme.colorScheme.surface),
                    disabledUncheckedTrackColor = WithmoTheme.colorScheme.surfaceContainerHighest
                        .copy(alpha = 0.12f)
                        .compositeOver(WithmoTheme.colorScheme.surface),
                    disabledUncheckedBorderColor = WithmoTheme.colorScheme.onSurface
                        .copy(alpha = 0.12f)
                        .compositeOver(WithmoTheme.colorScheme.surface),
                    disabledUncheckedIconColor = WithmoTheme.colorScheme.surfaceContainerHighest
                        .copy(alpha = 0.38f)
                        .compositeOver(WithmoTheme.colorScheme.surface),
                ),
            )
        }
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithSwitchLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoSettingItemWithSwitch(
            title = "設定項目",
            checked = false,
            onCheckedChange = {},
        )
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithSwitchDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoSettingItemWithSwitch(
            title = "設定項目",
            checked = true,
            onCheckedChange = {},
        )
    }
}
