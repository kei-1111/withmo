package io.github.kei_1111.withmo.feature.home.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.App
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.component.page_content.PagerContent
import kotlinx.collections.immutable.toPersistentList

private const val BOTTOM_SHEET_SHOW_DRAG_HEIGHT = -50f

@Suppress("LongMethod")
@Composable
internal fun HomeScreenContent(
    state: HomeState.Stable,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPaddingValue)

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        if (dragAmount < BOTTOM_SHEET_SHOW_DRAG_HEIGHT) {
                            onAction(HomeAction.OnAppListSheetSwipeUp)
                        }
                    },
                )
            },
    ) {
        PagerContent(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        if (!state.isChangeModelScaleContentShown && !state.isEditMode && state.favoriteAppInfoList.isNotEmpty()) {
            RowAppList(
                state = state,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = bottomPaddingValue),
            )
        }
    }
}

@Composable
private fun RowAppList(
    state: HomeState.Stable,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appIconSettings = state.currentUserSettings.appIconSettings
    val appList = state.favoriteAppInfoList

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 4.dp),
        horizontalArrangement = if (appList.size == 1) { Arrangement.Center } else { Arrangement.SpaceBetween },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        appList.forEach {
            App(
                onClick = { onAction(HomeAction.OnAppClick(it.info)) },
                onLongClick = { onAction(HomeAction.OnAppLongClick(it.info)) },
                appInfo = it.info,
                isNotificationBadgeShown = it.info.notification,
                appIconShape = appIconSettings.appIconShape.toShape(
                    roundedCornerPercent = appIconSettings.roundedCornerPercent,
                ),
                isAppNameShown = false,
            )
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun HomeScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        HomeScreenContent(
            state = HomeState.Stable(
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
            ),
            onAction = { },
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun HomeScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        HomeScreenContent(
            state = HomeState.Stable(
                favoriteAppInfoList = List(3) {
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
            ),
            onAction = { },
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun RowAppListLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        RowAppList(
            state = HomeState.Stable(
                favoriteAppInfoList = List(4) {
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
            ),
            onAction = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun RowAppListDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        RowAppList(
            state = HomeState.Stable(
                favoriteAppInfoList = List(1) {
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
            ),
            onAction = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
