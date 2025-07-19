package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.LocalClickBlocker

@Composable
fun WithmoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.filledTonalShape,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    elevation: Dp? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable (RowScope.() -> Unit),
) {
    val clickBlocker = LocalClickBlocker.current

    FilledTonalButton(
        onClick = {
            if (clickBlocker.tryClick()) {
                onClick()
            }
        },
        modifier = modifier
            .then(
                if (elevation != null) {
                    Modifier.withmoShadow(radius = elevation)
                } else {
                    Modifier
                },
            ),
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        content()
    }
}

@Composable
fun WithmoSaveButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    WithmoButton(
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
private fun WithmoButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoButton(
            onClick = {},
        ) {}
    }
}

@Preview
@Composable
private fun WithmoButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoButton(
            onClick = {},
        ) {}
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
