package io.github.kei_1111.withmo.ui.screens.home.component

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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.model.toDateTimeInfo
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.ui.component.AppItem
import io.github.kei_1111.withmo.ui.component.WithmoClock
import io.github.kei_1111.withmo.ui.component.WithmoIconButton
import io.github.kei_1111.withmo.ui.composition.LocalCurrentTime
import io.github.kei_1111.withmo.ui.screens.home.HomeAction
import io.github.kei_1111.withmo.ui.screens.home.HomeState
import io.github.kei_1111.withmo.ui.screens.home.component.page_content.PagerContent
import io.github.kei_1111.withmo.ui.theme.dimensions.Alphas
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

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
        if (state.isCloseScaleSliderButtonShown) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                WithmoIconButton(
                    onClick = { onAction(HomeAction.OnCloseScaleSliderButtonClick) },
                    modifier = Modifier
                        .padding(start = Paddings.Medium)
                        .size(AppConstants.DefaultAppIconSize.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
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
