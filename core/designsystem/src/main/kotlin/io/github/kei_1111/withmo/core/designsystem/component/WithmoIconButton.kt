package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

@Composable
fun WithmoIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = WithmoTheme.colorScheme.surface,
    contentColor: Color = WithmoTheme.colorScheme.onSurface,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(containerColor)
            .safeClickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
            ),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
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
                tint = WithmoTheme.colorScheme.onSurface,
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
                tint = WithmoTheme.colorScheme.onSurface,
            )
        }
    }
}
