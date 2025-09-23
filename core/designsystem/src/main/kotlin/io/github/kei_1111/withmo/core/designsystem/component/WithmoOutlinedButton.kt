package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.LocalClickBlocker

@Composable
fun WithmoOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    elevation: Dp? = null,
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val clickBlocker = LocalClickBlocker.current
//    val context = LocalContext.current

    OutlinedButton(
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
fun WithmoBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoOutlinedButton(
        onClick = onClick,
        modifier = modifier.height(CommonDimensions.SettingItemHeight),
    ) {
        BodyMediumText("戻る")
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
private fun WithmoBackButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoBackButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun WithmoBackButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoBackButton(
            onClick = {},
        )
    }
}
