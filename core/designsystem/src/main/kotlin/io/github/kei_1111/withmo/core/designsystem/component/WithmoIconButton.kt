package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

@Composable
fun WithmoIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .withmoShadow(
                shape = CircleShape,
            )
            .background(
                color = backgroundColor,
                shape = CircleShape,
            )
            .safeClickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview
@Composable
private fun WithmoIconButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoIconButton(
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun WithmoIconButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoIconButton(
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
