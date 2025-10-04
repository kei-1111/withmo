package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private sealed interface AppListItem {
    val key: String

    data class Header(val title: String, override val key: String) : AppListItem
    data class AppItem(val appInfo: AppInfo, override val key: String) : AppListItem
    data object Spacer : AppListItem {
        override val key: String = "spacer"
    }
}

@Composable
fun AppList(
    appList: ImmutableList<AppInfo>,
    appContent: @Composable LazyGridItemScope.(AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(top = 4.dp, bottom = 16.dp),
    isShowCustomizeApp: Boolean = true,
) {
    val context = LocalContext.current
    val distinctAppList = appList.distinctBy { it.packageName }

    val launchableApps = distinctAppList.filter { it.packageName != context.packageName }
    val settingApps = distinctAppList.filter { it.packageName == context.packageName }

    val items = buildList {
        if (launchableApps.isNotEmpty()) {
            add(AppListItem.Header("アプリ一覧", "header_launchable"))
            launchableApps.forEach { add(AppListItem.AppItem(it, it.packageName)) }
        }
        if (settingApps.isNotEmpty() && isShowCustomizeApp) {
            add(AppListItem.Spacer)
            add(AppListItem.Header("カスタマイズ", "header_customize"))
            settingApps.forEach { add(AppListItem.AppItem(it, it.packageName)) }
        }
    }

    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    LazyVerticalGrid(
        columns = GridCells.Fixed(DesignConstants.APP_LIST_GRID_COLUMNS),
        modifier = modifier.nestedScroll(nestedScrollConnection),
        state = lazyGridState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(
            items = items,
            key = { it.key },
            span = { item ->
                when (item) {
                    is AppListItem.Header, AppListItem.Spacer -> GridItemSpan(maxLineSpan)
                    is AppListItem.AppItem -> GridItemSpan(1)
                }
            },
        ) { item ->
            when (item) {
                is AppListItem.Header -> {
                    Text(
                        text = item.title,
                        modifier = Modifier.animateItem(),
                        color = WithmoTheme.colorScheme.onSurface,
                        style = WithmoTheme.typography.labelMedium,
                    )
                }
                is AppListItem.AppItem -> {
                    appContent(this, item.appInfo)
                }
                AppListItem.Spacer -> {}
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
