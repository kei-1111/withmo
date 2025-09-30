package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.theme.DesignConstants
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun AppList(
    appList: ImmutableList<AppInfo>,
    appContent: @Composable (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(top = 4.dp, bottom = 16.dp),
    isShowCustomizeApp: Boolean = true,
) {
    val context = LocalContext.current
    val launchableAppList = appList
        .filter { it.packageName != context.packageName }
        .toPersistentList()
    val settingApp = appList
        .filter { it.packageName == context.packageName }
        .toPersistentList()

    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    val movableAppContent = remember(appContent) {
        movableContentOf<AppInfo> { item ->
            appContent(item)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(DesignConstants.APP_LIST_GRID_COLUMNS),
        modifier = modifier.nestedScroll(nestedScrollConnection),
        state = lazyGridState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (launchableAppList.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "アプリ一覧",
                    color = WithmoTheme.colorScheme.onSurface,
                    style = WithmoTheme.typography.labelMedium,
                )
            }
            items(
                items = launchableAppList,
                key = { it.packageName },
            ) { item ->
                movableAppContent(item)
            }
        }
        if (settingApp.isNotEmpty() && isShowCustomizeApp) {
            item(span = { GridItemSpan(maxLineSpan) }) {}
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "カスタマイズ",
                    color = WithmoTheme.colorScheme.onSurface,
                    style = WithmoTheme.typography.labelMedium,
                )
            }
            items(
                items = settingApp,
                key = { it.packageName },
            ) { item ->
                movableAppContent(item)
            }
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun AppListLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        AppList(
            appList = List(18) {
                AppInfo(
                    appIcon = appIcon,
                    label = "アプリ $it",
                    packageName = if (it == 0) context.packageName else "io.github.kei_1111.withmo.app$it",
                    notification = it % 3 == 0,
                )
            }.toPersistentList(),
            appContent = {
                App(
                    appInfo = it,
                    appIconShape = CircleShape,
                    isNotificationBadgeShown = false,
                )
            },
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun AppListDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        AppList(
            appList = List(18) {
                AppInfo(
                    appIcon = appIcon,
                    label = "アプリ $it",
                    packageName = if (it == 0) context.packageName else "io.github.kei_1111.withmo.app$it",
                    notification = it % 3 == 0,
                )
            }.toPersistentList(),
            appContent = {
                App(
                    appInfo = it,
                    appIconShape = CircleShape,
                    isNotificationBadgeShown = false,
                )
            },
        )
    }
}
