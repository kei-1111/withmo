package io.github.kei_1111.withmo.feature.home.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.component.App
import io.github.kei_1111.withmo.core.designsystem.component.LabelMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.DesignConstants
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteOrder
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.feature.home.preview.HomeDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.home.preview.HomeLightPreviewEnvironment
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun AppList(
    appList: ImmutableList<WithmoAppInfo>,
    userSettings: UserSettings,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val launchableAppList = appList
        .filter { it.info.packageName != context.packageName }
        .toPersistentList()
    val settingApp = appList
        .filter { it.info.packageName == context.packageName }
        .toPersistentList()

    val appIconShape = userSettings.appIconSettings.appIconShape.toShape(
        userSettings.appIconSettings.roundedCornerPercent,
    )

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        if (launchableAppList.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
            ) {
                LabelMediumText(
                    text = "アプリ一覧",
                )
                CustomAppInfoGridLayout(
                    items = launchableAppList,
                    columns = DesignConstants.AppListGridColums,
                    appIconShape = appIconShape,
                    isNotificationBadgeShown =
                    userSettings.notificationSettings.isNotificationBadgeEnabled,
                    onClick = onAppClick,
                    onLongClick = onAppLongClick,
                )
            }
        }
        if (settingApp.isNotEmpty() &&
            !userSettings.sideButtonSettings.isNavigateSettingsButtonShown
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
            ) {
                LabelMediumText(
                    text = "カスタマイズ",
                )
                CustomAppInfoGridLayout(
                    items = settingApp,
                    columns = DesignConstants.AppListGridColums,
                    appIconShape = appIconShape,
                    isNotificationBadgeShown = false,
                    onClick = onAppClick,
                    onLongClick = onAppLongClick,
                )
            }
        }
    }
}

@Composable
private fun CustomAppInfoGridLayout(
    items: ImmutableList<WithmoAppInfo>,
    columns: Int,
    appIconShape: Shape,
    isNotificationBadgeShown: Boolean,
    onClick: (AppInfo) -> Unit,
    onLongClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(bottom = Paddings.ExtraSmall),
        verticalArrangement = Arrangement.spacedBy(Paddings.Large),
    ) {
        items.chunked(columns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Paddings.Large),
            ) {
                rowItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(Weights.Medium),
                        contentAlignment = Alignment.Center,
                    ) {
                        App(
                            appInfo = item.info,
                            isNotificationBadgeShown = isNotificationBadgeShown,
                            onClick = { onClick(item.info) },
                            onLongClick = { onLongClick(item.info) },
                            appIconShape = appIconShape,
                        )
                    }
                }
                if (rowItems.size < columns) {
                    repeat(columns - rowItems.size) {
                        Spacer(modifier = Modifier.weight(Weights.Medium))
                    }
                }
            }
        }
    }
}

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun AppListLightPreview() {
    HomeLightPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, io.github.kei_1111.withmo.core.designsystem.R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        AppList(
            appList = List(18) {
                WithmoAppInfo(
                    info = AppInfo(
                        appIcon = appIcon,
                        label = "アプリ $it",
                        packageName = if (it == 0) context.packageName else "io.github.kei_1111.withmo.app$it",
                        notification = it % 3 == 0,
                    ),
                    favoriteOrder = FavoriteOrder.NotFavorite,
                    position = Offset.Unspecified,
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
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun AppListDarkPreview() {
    HomeDarkPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, io.github.kei_1111.withmo.core.designsystem.R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        AppList(
            appList = List(18) {
                WithmoAppInfo(
                    info = AppInfo(
                        appIcon = appIcon,
                        label = "アプリ $it",
                        packageName = if (it == 0) context.packageName else "io.github.kei_1111.withmo.app$it",
                        notification = it % 3 == 0,
                    ),
                    favoriteOrder = FavoriteOrder.NotFavorite,
                    position = Offset.Unspecified,
                )
            }.toPersistentList(),
            userSettings = UserSettings(),
            onAppClick = {},
            onAppLongClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
