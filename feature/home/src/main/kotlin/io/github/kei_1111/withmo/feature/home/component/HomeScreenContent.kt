package io.github.kei_1111.withmo.feature.home.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.App
import io.github.kei_1111.withmo.core.designsystem.component.WithmoClock
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteOrder
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import io.github.kei_1111.withmo.core.model.toDateTimeInfo
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.core.ui.LocalCurrentTime
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.component.page_content.ChangeModelScaleContent
import io.github.kei_1111.withmo.feature.home.component.page_content.PagerContent
import io.github.kei_1111.withmo.feature.home.preview.HomeDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.home.preview.HomeLightPreviewEnvironment
import kotlinx.collections.immutable.toPersistentList

private const val BottomSheetShowDragHeight = -50f

@Suppress("LongMethod")
@Composable
internal fun HomeScreenContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPaddingValue)

    Box(
        modifier = modifier,
    ) {
        if (state.isChangeModelScaleContentShown) {
            ChangeModelScaleContent(
                state = state,
                onAction = onAction,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                if (dragAmount < BottomSheetShowDragHeight) {
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
                        .weight(Weights.Medium),
                )
                if (!state.isEditMode && state.favoriteAppList.isNotEmpty()) {
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
    }
}

@Composable
private fun RowAppList(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appIconSettings = state.currentUserSettings.appIconSettings

    Row(
        modifier = modifier
            .padding(vertical = Paddings.ExtraSmall)
            .padding(horizontal = Paddings.Medium)
            .background(
                color = if (appIconSettings.isFavoriteAppBackgroundShown) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = Alphas.Medium)
                } else {
                    Color.Transparent
                },
                shape = MaterialTheme.shapes.medium,
            )
            .padding(vertical = Paddings.Small),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        state.favoriteAppList.forEach {
            App(
                onClick = { onAction(HomeAction.OnAppClick(it.info)) },
                onLongClick = { onAction(HomeAction.OnAppLongClick(it.info)) },
                appInfo = it.info,
                appIconSize = appIconSettings.appIconSize,
                appIconShape = appIconSettings.appIconShape.toShape(
                    roundedCornerPercent = appIconSettings.roundedCornerPercent,
                ),
                isAppNameShown = appIconSettings.isAppNameShown,
                modifier = Modifier.weight(Weights.Medium),
            )
        }
    }
}

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun HomeScreenContentLightPreview() {
    HomeLightPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        HomeScreenContent(
            state = HomeState(
                favoriteAppList = List(3) {
                    WithmoAppInfo(
                        info = AppInfo(
                            appIcon = appIcon,
                            label = "アプリ $it",
                            packageName = "io.github.kei_1111.withmo.app$it",
                            notification = it % 2 == 0,
                        ),
                        favoriteOrder = FavoriteOrder.NotFavorite,
                        position = Offset.Unspecified,
                    )
                }.toPersistentList(),
            ),
            onAction = { },
        )
    }
}

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun HomeScreenContentDarkPreview() {
    HomeDarkPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        HomeScreenContent(
            state = HomeState(
                favoriteAppList = List(3) {
                    WithmoAppInfo(
                        info = AppInfo(
                            appIcon = appIcon,
                            label = "アプリ $it",
                            packageName = "io.github.kei_1111.withmo.app$it",
                            notification = it % 2 == 0,
                        ),
                        favoriteOrder = FavoriteOrder.NotFavorite,
                        position = Offset.Unspecified,
                    )
                }.toPersistentList(),
            ),
            onAction = { },
        )
    }
}

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun RowAppListLightPreview() {
    HomeLightPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        RowAppList(
            state = HomeState(
                favoriteAppList = List(4) {
                    WithmoAppInfo(
                        info = AppInfo(
                            appIcon = appIcon,
                            label = "アプリ $it",
                            packageName = "io.github.kei_1111.withmo.app$it",
                            notification = it % 2 == 0,
                        ),
                        favoriteOrder = FavoriteOrder.NotFavorite,
                        position = Offset.Unspecified,
                    )
                }.toPersistentList(),
            ),
            onAction = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun RowAppListDarkPreview() {
    HomeDarkPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        RowAppList(
            state = HomeState(
                favoriteAppList = List(4) {
                    WithmoAppInfo(
                        info = AppInfo(
                            appIcon = appIcon,
                            label = "アプリ $it",
                            packageName = "io.github.kei_1111.withmo.app$it",
                            notification = it % 2 == 0,
                        ),
                        favoriteOrder = FavoriteOrder.NotFavorite,
                        position = Offset.Unspecified,
                    )
                }.toPersistentList(),
            ),
            onAction = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
