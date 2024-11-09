package com.example.withmo.ui.component.favorite_settings

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
import androidx.compose.ui.unit.dp
import com.example.withmo.ui.theme.UiConfig

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
                    UiConfig.BorderWidth,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = UiConfig.DisabledContentAlpha),
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(UiConfig.ExtraSmallPadding),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size((UiConfig.DefaultAppIconSize + UiConfig.AppIconPadding).dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = UiConfig.DisabledContentAlpha),
                )
            }
        }
    }
}
