package io.github.kei_1111.withmo.feature.home.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.AppItem
import io.github.kei_1111.withmo.core.designsystem.component.WithmoClock
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.toDateTimeInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.core.ui.LocalCurrentTime
import io.github.kei_1111.withmo.core.ui.PreviewEnvironment
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.component.page_content.ChangeModelScaleContent
import io.github.kei_1111.withmo.feature.home.component.page_content.PagerContent
import kotlinx.collections.immutable.toPersistentList

private const val BottomSheetShowDragHeight = -50f

@RequiresApi(Build.VERSION_CODES.O)
@Suppress("LongMethod")
@Composable
internal fun HomeScreenContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentTime = LocalCurrentTime.current

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
            if (state.currentUserSettings.clockSettings.isClockShown) {
                WithmoClock(
                    clockType = state.currentUserSettings.clockSettings.clockType,
                    dateTimeInfo = currentTime.toDateTimeInfo(),
                    modifier = Modifier.padding(start = Paddings.Medium),
                )
            }
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
                        modifier = Modifier.fillMaxWidth(),
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
            AppItem(
                onClick = { onAction(HomeAction.OnAppClick(it)) },
                onLongClick = { onAction(HomeAction.OnAppLongClick(it)) },
                appInfo = it,
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
@Composable
@Preview
private fun HomeScreenContentPreview() {
    PreviewEnvironment {
        WithmoTheme(themeType = ThemeType.LIGHT) {
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
                        AppInfo(
                            appIcon = appIcon,
                            label = "withmo $it",
                            packageName = "com.example.app$it",
                        )
                    }.toPersistentList(),
                ),
                onAction = { },
            )
        }
    }
}

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun RowAppListPreview() {
    PreviewEnvironment {
        WithmoTheme(themeType = ThemeType.LIGHT) {
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
                        AppInfo(
                            appIcon = appIcon,
                            label = "withmo $it",
                            packageName = "com.example.app$it",
                        )
                    }.toPersistentList(),
                ),
                onAction = { },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
