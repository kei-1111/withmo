package io.github.kei_1111.withmo.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.component.App
import io.github.kei_1111.withmo.core.designsystem.component.LabelMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.DesignConstants
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Suppress("LongMethod")
@Composable
internal fun AppList(
    appList: ImmutableList<AppInfo>,
    userSettings: UserSettings,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val launchableAppList = appList
        .filter { it.packageName != context.packageName }
        .toPersistentList()
    val settingApp = appList
        .filter { it.packageName == context.packageName }
        .toPersistentList()

    val appIconShape = userSettings.appIconSettings.appIconShape.toShape(
        userSettings.appIconSettings.roundedCornerPercent,
    )

    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    LazyVerticalGrid(
        columns = GridCells.Fixed(DesignConstants.AppListGridColums),
        modifier = modifier.nestedScroll(nestedScrollConnection),
        contentPadding = PaddingValues(
            start = Paddings.Medium,
            end = Paddings.Medium,
            bottom = Paddings.Medium + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
        ),
        verticalArrangement = Arrangement.spacedBy(Paddings.Large),
        horizontalArrangement = Arrangement.spacedBy(Paddings.Large),
    ) {
        if (launchableAppList.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LabelMediumText(
                    text = "アプリ一覧",
                )
            }
            items(launchableAppList) { item ->
                App(
                    appInfo = item,
                    appIconShape = appIconShape,
                    isNotificationBadgeShown =
                    userSettings.notificationSettings.isNotificationBadgeEnabled,
                    onClick = { onAppClick(item) },
                    onLongClick = { onAppLongClick(item) },
                )
            }
        }
        if (settingApp.isNotEmpty() &&
            !userSettings.sideButtonSettings.isNavigateSettingsButtonShown
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {}
            item(span = { GridItemSpan(maxLineSpan) }) {
                LabelMediumText(
                    text = "カスタマイズ",
                )
            }
            items(settingApp) { item ->
                App(
                    appInfo = item,
                    appIconShape = appIconShape,
                    isNotificationBadgeShown = false,
                    onClick = { onAppClick(item) },
                    onLongClick = { onAppLongClick(item) },
                )
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
                foregroundIcon = ContextCompat.getDrawable(context, io.github.kei_1111.withmo.core.designsystem.R.drawable.withmo_icon_wide)!!,
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
            userSettings = UserSettings(),
            onAppClick = {},
            onAppLongClick = {},
            modifier = Modifier.fillMaxSize(),
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
                foregroundIcon = ContextCompat.getDrawable(context, io.github.kei_1111.withmo.core.designsystem.R.drawable.withmo_icon_wide)!!,
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
            userSettings = UserSettings(),
            onAppClick = {},
            onAppLongClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
