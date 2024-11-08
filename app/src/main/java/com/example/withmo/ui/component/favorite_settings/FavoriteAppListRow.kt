package com.example.withmo.ui.component.favorite_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FavoriteAppListRow(
    favoriteAppList: ImmutableList<AppInfo>,
    removeSelectedAppList: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    appIconShape: Shape = CircleShape,
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = UiConfig.MediumPadding,
                vertical = UiConfig.SmallPadding,
            ),
        horizontalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
    ) {
        favoriteAppList.forEach {
            FavoriteAppSelectorItem(
                appInfo = it,
                isSelected = true,
                addSelectedAppList = { },
                removeSelectedAppList = { removeSelectedAppList(it) },
                onClick = { removeSelectedAppList(it) },
                backgroundColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.weight(UiConfig.DefaultWeight),
                appIconShape = appIconShape,
            )
        }

        repeat(UiConfig.FavoriteAppListMaxSize - favoriteAppList.size) {
            EmptyAppItem(
                modifier = Modifier.weight(UiConfig.DefaultWeight),
            )
        }
    }
}
