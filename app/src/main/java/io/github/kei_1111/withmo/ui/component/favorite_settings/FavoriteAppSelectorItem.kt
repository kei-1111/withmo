package io.github.kei_1111.withmo.ui.component.favorite_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.ui.component.AppItem
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
fun FavoriteAppSelectorItem(
    appInfo: AppInfo,
    isSelected: Boolean,
    addSelectedAppList: () -> Unit,
    removeSelectedAppList: () -> Unit,
    onClick: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    appIconShape: Shape = CircleShape,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AppItem(
            appInfo = appInfo,
            modifier = getFavoriteAppSelectorItemModifier(
                isSelected = isSelected,
                addSelectedAppList = addSelectedAppList,
                removeSelectedAppList = removeSelectedAppList,
                backgroundColor = backgroundColor,
            ),
            appIconShape = appIconShape,
            onClick = onClick,
        )
    }
}

private val BorderWidth = 1.dp

@Composable
fun getFavoriteAppSelectorItemModifier(
    isSelected: Boolean,
    addSelectedAppList: () -> Unit,
    removeSelectedAppList: () -> Unit,
    backgroundColor: Color,
): Modifier {
    return if (isSelected) {
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { removeSelectedAppList() }
            .border(
                BorderWidth,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.medium,
            )
            .background(backgroundColor)
            .padding(Paddings.ExtraSmall)
    } else {
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { addSelectedAppList() }
            .padding(Paddings.ExtraSmall)
    }
}
