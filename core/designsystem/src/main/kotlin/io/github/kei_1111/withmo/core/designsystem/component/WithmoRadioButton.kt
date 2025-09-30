package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
fun WithmoRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = WithmoRadioButtonDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

object WithmoRadioButtonDefaults {
    @Composable
    fun colors(
        selectedColor: Color = WithmoTheme.colorScheme.primary,
        unselectedColor: Color = WithmoTheme.colorScheme.onSurfaceVariant,
        disabledSelectedColor: Color = WithmoTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        disabledUnselectedColor: Color = WithmoTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
    ): RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = selectedColor,
        unselectedColor = unselectedColor,
        disabledSelectedColor = disabledSelectedColor,
        disabledUnselectedColor = disabledUnselectedColor,
    )
}

@Composable
@Preview
private fun WithmoRadioButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoRadioButton(
            selected = true,
            onClick = {},
        )
    }
}

@Composable
@Preview
private fun WithmoRadioButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoRadioButton(
            selected = true,
            onClick = {},
        )
    }
}
