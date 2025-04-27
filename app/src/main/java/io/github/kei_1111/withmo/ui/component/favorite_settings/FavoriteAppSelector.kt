package io.github.kei_1111.withmo.ui.component.favorite_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.theme.DesignConstants
import io.github.kei_1111.withmo.ui.theme.dimensions.Alphas
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
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
    val sortedAppList = appList.sortedBy(AppInfo::label)

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(DesignConstants.AppListGridColums),
        verticalArrangement = Arrangement.spacedBy(Paddings.Large),
        horizontalArrangement = Arrangement.spacedBy(Paddings.Large),
        contentPadding = PaddingValues(
            top = Paddings.ExtraSmall,
            bottom = Paddings.Medium,
        ),
    ) {
        items(sortedAppList) { appInfo ->
            val isSelected = favoriteAppList
                .any { it.packageName == appInfo.packageName }

            FavoriteAppSelectorItem(
                appInfo = appInfo,
                isSelected = isSelected,
                addSelectedAppList = { addSelectedAppList(appInfo) },
                removeSelectedAppList = {
                    removeSelectedAppList(appInfo)
                },
                onClick = {
                    if (isSelected) {
                        removeSelectedAppList(appInfo)
                    } else {
                        addSelectedAppList(appInfo)
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = Alphas.Disabled,
                ),
                modifier = Modifier.fillMaxWidth(),
                appIconShape = appIconShape,
            )
        }
    }
}
