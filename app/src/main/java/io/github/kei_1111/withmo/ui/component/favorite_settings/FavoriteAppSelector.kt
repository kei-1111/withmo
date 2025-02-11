package io.github.kei_1111.withmo.ui.component.favorite_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FavoriteAppSelector(
    appList: ImmutableList<AppInfo>,
    favoriteAppList: ImmutableList<AppInfo>,
    addSelectedAppList: (AppInfo) -> Unit,
    removeSelectedAppList: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    appIconShape: Shape = CircleShape,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(UiConfig.AppListScreenGridColums),
        verticalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
        horizontalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
        contentPadding = PaddingValues(
            top = UiConfig.ExtraSmallPadding,
            bottom = UiConfig.MediumPadding,
        ),
    ) {
        items(appList.size) { index ->
            FavoriteAppSelectorItem(
                appInfo = appList[index],
                isSelected = favoriteAppList
                    .any { it.packageName == appList[index].packageName },
                addSelectedAppList = { addSelectedAppList(appList[index]) },
                removeSelectedAppList = {
                    removeSelectedAppList(appList[index])
                },
                onClick = {
                    if (favoriteAppList
                            .any { it.packageName == appList[index].packageName }
                    ) {
                        removeSelectedAppList(appList[index])
                    } else {
                        addSelectedAppList(appList[index])
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = UiConfig.DisabledContentAlpha,
                ),
                modifier = Modifier.fillMaxWidth(),
                appIconShape = appIconShape,
            )
        }
    }
}
