package io.github.kei_1111.withmo.ui.screens.home

import android.content.Context
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChangeCircle
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.unity3d.player.UnityPlayer.UnitySendMessage
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.ui.component.LabelMediumText
import io.github.kei_1111.withmo.ui.component.WithmoIconButton
import io.github.kei_1111.withmo.ui.component.WithmoWidget
import io.github.kei_1111.withmo.ui.theme.UiConfig

@Suppress("LongMethod")
@Composable
fun PagerContent(
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val pagerState = rememberPagerState(pageCount = { UiConfig.PageCount })

    LaunchedEffect(pagerState) {
        var isFirstCollect = true
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (isFirstCollect) {
                isFirstCollect = false
            } else {
                when (page) {
                    0 -> {
                        UnitySendMessage("IKAnimationController", "TriggerEnterScreenAnimation", "")
                    }
                    1 -> {
                        UnitySendMessage("IKAnimationController", "TriggerExitScreenAnimation", "")
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(UiConfig.DefaultWeight),
        ) { page ->
            when (page) {
                0 -> {
                    DisplayModelContent(
                        uiState = uiState,
                        onEvent = onEvent,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = UiConfig.MediumPadding)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        val normalizedX = it.x / screenWidthPx
                                        val normalizedY = it.y / screenHeightPx

                                        UnitySendMessage(
                                            "IKAnimationController",
                                            "MoveLookat",
                                            "$normalizedX,$normalizedY",
                                        )
                                    },
                                    onLongPress = {
                                        UnitySendMessage("VRMAnimationController", "TriggerTouchAnimation", "")
                                    },
                                )
                            },
                    )
                }

                1 -> {
                    WidgetContent(
                        createWidgetView = createWidgetView,
                        uiState = uiState,
                        onEvent = onEvent,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        onEvent(HomeUiEvent.EnterEditMode)
                                    },
                                )
                            },
                    )
                }
            }
        }
        PageIndicator(
            pageCount = pagerState.pageCount,
            currentPage = pagerState.currentPage,
            isEditMode = uiState.isEditMode,
            exitEditMode = {
                onEvent(HomeUiEvent.ExitEditMode)
            },
            openWidgetList = {
                onEvent(HomeUiEvent.OpenWidgetListBottomSheet)
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    isEditMode: Boolean,
    exitEditMode: () -> Unit,
    openWidgetList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(UiConfig.PageIndicatorSpaceHeight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isEditMode) {
            LabelMediumText(
                text = "＋",
                modifier = Modifier
                    .padding(horizontal = UiConfig.MediumPadding)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape,
                    )
                    .clickable { openWidgetList() }
                    .padding(horizontal = UiConfig.MediumPadding),
            )
            LabelMediumText(
                text = "編集完了",
                modifier = Modifier
                    .padding(horizontal = UiConfig.MediumPadding)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape,
                    )
                    .clickable { exitEditMode() }
                    .padding(horizontal = UiConfig.MediumPadding),
            )
        } else {
            repeat(pageCount) { iteration ->
                val color =
                    if (currentPage == iteration) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = UiConfig.DisabledContentAlpha)
                    }
                Box(
                    modifier = Modifier
                        .padding(horizontal = UiConfig.MediumPadding)
                        .clip(CircleShape)
                        .background(color)
                        .size(UiConfig.PageIndicatorSize),
                )
            }
        }
    }
}

@Composable
private fun DisplayModelContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.LargePadding, Alignment.Bottom),
        horizontalAlignment = Alignment.End,
    ) {
        if (uiState.currentUserSettings.sideButtonSettings.isOpenDocumentButtonShown) {
            WithmoIconButton(
                onClick = {
                    onEvent(HomeUiEvent.OnOpenDocumentButtonClick)
                },
                icon = Icons.Rounded.ChangeCircle,
                modifier = Modifier.padding(start = UiConfig.MediumPadding),
            )
        }
        if (uiState.currentUserSettings.sideButtonSettings.isScaleSliderButtonShown) {
            WithmoIconButton(
                onClick = {
                    UnitySendMessage("Slidermaneger", "ShowObject", "")
                    onEvent(HomeUiEvent.SetShowScaleSlider(true))
                },
                icon = Icons.Rounded.Man,
            )
        }
    }
}

@Composable
private fun WidgetContent(
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appIconSpaceHeight = (uiState.currentUserSettings.appIconSettings.appIconSize + UiConfig.AppIconPadding).dp
    val bottomPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier,
    ) {
        uiState.widgetList.forEach { widgetInfo ->
            key(widgetInfo.id) {
                WithmoWidget(
                    widgetInfo = widgetInfo,
                    createWidgetView = createWidgetView,
                    startPadding = UiConfig.MediumPadding,
                    endPadding = UiConfig.MediumPadding,
                    bottomPadding = bottomPaddingValue + appIconSpaceHeight + UiConfig.PageIndicatorSpaceHeight,
                    isEditMode = uiState.isEditMode,
                    deleteWidget = { onEvent(HomeUiEvent.DeleteWidget(widgetInfo)) },
                    resizeWidget = { onEvent(HomeUiEvent.ResizeWidget(widgetInfo)) },
                )
            }
        }
    }
}
