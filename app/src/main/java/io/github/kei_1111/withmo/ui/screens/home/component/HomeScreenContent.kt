package io.github.kei_1111.withmo.ui.screens.home.component

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.unity3d.player.UnityPlayer.UnitySendMessage
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.model.toDateTimeInfo
import io.github.kei_1111.withmo.domain.model.user_settings.toShape
import io.github.kei_1111.withmo.ui.component.AppItem
import io.github.kei_1111.withmo.ui.component.WithmoClock
import io.github.kei_1111.withmo.ui.component.WithmoIconButton
import io.github.kei_1111.withmo.ui.composition.LocalCurrentTime
import io.github.kei_1111.withmo.ui.screens.home.HomeUiEvent
import io.github.kei_1111.withmo.ui.screens.home.HomeUiState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

private const val BottomSheetShowDragHeight = -50f

@RequiresApi(Build.VERSION_CODES.O)
@Suppress("LongMethod")
@Composable
internal fun HomeScreenContent(
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentTime = LocalCurrentTime.current

    Box(
        modifier = modifier,
    ) {
        if (uiState.isShowScaleSlider) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                WithmoIconButton(
                    onClick = {
                        UnitySendMessage("SliderManeger", "HideObject", "")
                        onEvent(HomeUiEvent.SetShowScaleSlider(false))
                    },
                    icon = Icons.Rounded.Close,
                    modifier = Modifier.padding(start = Paddings.Medium),
                )
            }
        } else {
            if (uiState.currentUserSettings.clockSettings.isClockShown) {
                WithmoClock(
                    clockType = uiState.currentUserSettings.clockSettings.clockType,
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
                                    onEvent(HomeUiEvent.OpenAppListBottomSheet)
                                }
                            },
                        )
                    },
            ) {
                PagerContent(
                    createWidgetView = createWidgetView,
                    uiState = uiState,
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(Weights.Medium),
                )
                RowAppList(
                    uiState = uiState,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun RowAppList(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appIconSettings = uiState.currentUserSettings.appIconSettings

    Row(
        modifier = modifier
            .padding(vertical = Paddings.ExtraSmall),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        uiState.favoriteAppList.forEach {
            AppItem(
                onClick = { onEvent(HomeUiEvent.StartApp(it)) },
                onLongClick = { onEvent(HomeUiEvent.DeleteApp(it)) },
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
