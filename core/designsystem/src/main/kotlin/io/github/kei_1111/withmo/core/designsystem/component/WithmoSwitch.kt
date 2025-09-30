package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

private const val SWITCH_SCALE = 0.75f

@Composable
fun WithmoSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumbContent: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: SwitchColors = WithmoSwitchDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.scale(SWITCH_SCALE),
        thumbContent = thumbContent,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

object WithmoSwitchDefaults {
    @Composable
    fun colors(
        checkedThumbColor: Color = WithmoTheme.colorScheme.onPrimary,
        checkedTrackColor: Color = WithmoTheme.colorScheme.primary,
        checkedBorderColor: Color = Color.Transparent,
        checkedIconColor: Color = WithmoTheme.colorScheme.onPrimaryContainer,
        uncheckedThumbColor: Color = WithmoTheme.colorScheme.outline,
        uncheckedTrackColor: Color = WithmoTheme.colorScheme.surfaceContainerHighest,
        uncheckedBorderColor: Color = WithmoTheme.colorScheme.outline,
        uncheckedIconColor: Color = WithmoTheme.colorScheme.surfaceContainerHighest,
        disabledCheckedThumbColor: Color = WithmoTheme.colorScheme.surface
            .compositeOver(WithmoTheme.colorScheme.surface),
        disabledCheckedTrackColor: Color = WithmoTheme.colorScheme.onSurface
            .copy(alpha = 0.12f)
            .compositeOver(WithmoTheme.colorScheme.surface),
        disabledCheckedBorderColor: Color = Color.Transparent,
        disabledCheckedIconColor: Color = WithmoTheme.colorScheme.onSurface
            .copy(alpha = 0.38f)
            .compositeOver(WithmoTheme.colorScheme.surface),
        disabledUncheckedThumbColor: Color = WithmoTheme.colorScheme.onSurface
            .copy(alpha = 0.38f)
            .compositeOver(WithmoTheme.colorScheme.surface),
        disabledUncheckedTrackColor: Color = WithmoTheme.colorScheme.surfaceContainerHighest
            .copy(alpha = 0.12f)
            .compositeOver(WithmoTheme.colorScheme.surface),
        disabledUncheckedBorderColor: Color = WithmoTheme.colorScheme.onSurface
            .copy(alpha = 0.12f)
            .compositeOver(WithmoTheme.colorScheme.surface),
        disabledUncheckedIconColor: Color = WithmoTheme.colorScheme.surfaceContainerHighest
            .copy(alpha = 0.38f)
            .compositeOver(WithmoTheme.colorScheme.surface),
    ): SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor,
        checkedBorderColor = checkedBorderColor,
        checkedIconColor = checkedIconColor,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        uncheckedBorderColor = uncheckedBorderColor,
        uncheckedIconColor = uncheckedIconColor,
        disabledCheckedThumbColor = disabledCheckedThumbColor,
        disabledCheckedTrackColor = disabledCheckedTrackColor,
        disabledCheckedBorderColor = disabledCheckedBorderColor,
        disabledCheckedIconColor = disabledCheckedIconColor,
        disabledUncheckedThumbColor = disabledUncheckedThumbColor,
        disabledUncheckedTrackColor = disabledUncheckedTrackColor,
        disabledUncheckedBorderColor = disabledUncheckedBorderColor,
        disabledUncheckedIconColor = disabledUncheckedIconColor,
    )
}

@Composable
@Preview
private fun WithmoSwitchLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoSwitch(
            checked = true,
            onCheckedChange = {},
        )
    }
}

@Composable
@Preview
private fun WithmoSwitchDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoSwitch(
            checked = true,
            onCheckedChange = {},
        )
    }
}
