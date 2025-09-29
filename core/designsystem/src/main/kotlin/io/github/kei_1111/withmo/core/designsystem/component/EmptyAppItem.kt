package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

private val BorderWidth = 1.dp

@Composable
fun EmptyAppItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .border(
                    BorderWidth,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun EmptyAppItemLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        EmptyAppItem()
    }
}

@Preview
@Composable
private fun EmptyAppItemDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        EmptyAppItem()
    }
}
