package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.LocalClickBlocker

@Composable
fun WithmoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.filledTonalShape,
    colors: ButtonColors = WithmoButtonDefaults.buttonColors(),
    elevation: Dp? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable (RowScope.() -> Unit),
) {
    val clickBlocker = LocalClickBlocker.current

    FilledTonalButton(
        onClick = { clickBlocker.processClick(onClick) },
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
fun WithmoOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = WithmoButtonDefaults.outlinedButtonColors(),
    elevation: Dp? = null,
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val clickBlocker = LocalClickBlocker.current

    OutlinedButton(
        onClick = { clickBlocker.processClick(onClick) },
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

object WithmoButtonDefaults {
    @Composable
    fun buttonColors(
        containerColor: Color = WithmoTheme.colorScheme.secondaryContainer,
        contentColor: Color = WithmoTheme.colorScheme.primary,
        disabledContainerColor: Color = WithmoTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledContentColor: Color = WithmoTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    ): ButtonColors = ButtonDefaults.filledTonalButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun outlinedButtonColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = WithmoTheme.colorScheme.primary,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = WithmoTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    ): ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )
}

@Composable
fun WithmoSaveButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    WithmoButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        enabled = enabled,
    ) {
        Text(
            text = "保存",
            style = WithmoTheme.typography.bodyMedium,
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
private fun WithmoOutlinedButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoOutlinedButton(
            onClick = {},
        ) {}
    }
}

@Preview
@Composable
private fun WithmoOutlinedButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoOutlinedButton(
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
