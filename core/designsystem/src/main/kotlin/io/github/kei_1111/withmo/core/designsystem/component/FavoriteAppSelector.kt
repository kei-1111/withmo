package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.theme.DesignConstants
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun FavoriteAppSelector(
    appList: ImmutableList<AppInfo>,
    favoriteAppInfoList: ImmutableList<FavoriteAppInfo>,
    addSelectedAppList: (AppInfo) -> Unit,
    removeSelectedAppList: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    appIconShape: Shape = CircleShape,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(DesignConstants.APP_LIST_GRID_COLUMNS),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(
            top = 4.dp,
            bottom = 16.dp,
        ),
    ) {
        items(appList) { appInfo ->
            val isSelected = favoriteAppInfoList
                .any { it.info.packageName == appInfo.packageName }

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
                    alpha = 0.38f,
                ),
                modifier = Modifier.fillMaxWidth(),
                appIconShape = appIconShape,
            )
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun FavoriteAppSelectorLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        FavoriteAppSelector(
            appList = List(40) {
                AppInfo(
                    appIcon = appIcon,
                    label = "アプリ $it",
                    packageName = "io.github.kei_1111.withmo.app$it",
                    notification = it % 2 == 0,
                )
            }.toPersistentList(),
            favoriteAppInfoList = List(2) {
                FavoriteAppInfo(
                    info = AppInfo(
                        appIcon = appIcon,
                        label = "アプリ $it",
                        packageName = "io.github.kei_1111.withmo.app$it",
                        notification = it % 2 == 0,
                    ),
                    favoriteOrder = it,
                )
            }.toPersistentList(),
            addSelectedAppList = { },
            removeSelectedAppList = { },
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun FavoriteAppSelectorDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        FavoriteAppSelector(
            appList = List(40) {
                AppInfo(
                    appIcon = appIcon,
                    label = "アプリ $it",
                    packageName = "io.github.kei_1111.withmo.app$it",
                    notification = it % 2 == 0,
                )
            }.toPersistentList(),
            favoriteAppInfoList = List(2) {
                FavoriteAppInfo(
                    info = AppInfo(
                        appIcon = appIcon,
                        label = "アプリ $it",
                        packageName = "io.github.kei_1111.withmo.app$it",
                        notification = it % 2 == 0,
                    ),
                    favoriteOrder = it,
                )
            }.toPersistentList(),
            addSelectedAppList = { },
            removeSelectedAppList = { },
        )
    }
}
