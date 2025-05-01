package io.github.kei_1111.withmo.ui.component.favorite_settings

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
import io.github.kei_1111.withmo.common.AppConstants
import io.github.kei_1111.withmo.ui.theme.dimensions.Alphas
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

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
                    MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled),
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(Paddings.ExtraSmall),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(AppConstants.DefaultAppIconSize.dp + Paddings.Large),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled),
                )
            }
        }
    }
}
