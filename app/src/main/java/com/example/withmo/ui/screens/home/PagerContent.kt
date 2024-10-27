package com.example.withmo.ui.screens.home

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
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.withmo.domain.model.WidgetInfo
import com.example.withmo.ui.component.LabelMediumText
import com.example.withmo.ui.component.WithmoIconButton
import com.example.withmo.ui.component.WithmoWidget
import com.example.withmo.ui.theme.UiConfig
import com.unity3d.player.UnityPlayer.UnitySendMessage
import kotlinx.collections.immutable.ImmutableList

@Suppress("LongMethod")
@Composable
fun PagerContent(
    isScaleSliderButtonShown: Boolean,
    isSortButtonShown: Boolean,
    showScaleSlider: () -> Unit,
    popupExpand: () -> Unit,
    openActionSelectionBottomSheet: () -> Unit,
    displayedWidgetList: ImmutableList<WidgetInfo>,
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    appIconSize: Float,
    isEditMode: Boolean,
    exitEditMode: () -> Unit,
    deleteWidget: (WidgetInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { UiConfig.PageCount })

    LaunchedEffect(pagerState) {
        var isFirstCollect = true
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (isFirstCollect) {
                isFirstCollect = false
            } else {
                when (page) {
                    0 -> {
                        UnitySendMessage("AnimationController", "TriggerEnterScreenAnimation", "")
                    }
                    1 -> {
                        UnitySendMessage("AnimationController", "TriggerExitScreenAnimation", "")
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = UiConfig.MediumPadding),
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
                        isScaleSliderButtonShown = isScaleSliderButtonShown,
                        isSortButtonShown = isSortButtonShown,
                        showScaleSlider = showScaleSlider,
                        popupExpand = popupExpand,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        UnitySendMessage("AnimationController", "TriggerTouchAnimation", "")
                                    },
                                )
                            },
                    )
                }

                1 -> {
                    WidgetContent(
                        displayedWidgetList = displayedWidgetList,
                        createWidgetView = createWidgetView,
                        appIconSize = appIconSize,
                        isEditMode = isEditMode,
                        deleteWidget = deleteWidget,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        openActionSelectionBottomSheet()
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
            isEditMode = isEditMode,
            exitEditMode = exitEditMode,
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
                text = "編集完了",
                modifier = Modifier
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
    isScaleSliderButtonShown: Boolean,
    isSortButtonShown: Boolean,
    showScaleSlider: () -> Unit,
    popupExpand: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.LargePadding, Alignment.Bottom),
        horizontalAlignment = Alignment.End,
    ) {
        if (isScaleSliderButtonShown) {
            WithmoIconButton(
                onClick = showScaleSlider,
                icon = Icons.Default.Man,
            )
        }
        if (isSortButtonShown) {
            WithmoIconButton(
                onClick = popupExpand,
                icon = Icons.Default.Tune,
            )
        }
    }
}

@Composable
private fun WidgetContent(
    displayedWidgetList: ImmutableList<WidgetInfo>,
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    appIconSize: Float,
    isEditMode: Boolean,
    deleteWidget: (WidgetInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appIconSpaceHeight = (appIconSize + UiConfig.AppIconPadding).dp
    val bottomPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier,
    ) {
        displayedWidgetList.forEach { widgetInfo ->
            key(widgetInfo.id) {
                WithmoWidget(
                    widgetInfo = widgetInfo,
                    createWidgetView = createWidgetView,
                    endPadding = UiConfig.MediumPadding + UiConfig.MediumPadding,
                    bottomPadding = bottomPaddingValue + appIconSpaceHeight + UiConfig.PageIndicatorSpaceHeight,
                    isEditMode = isEditMode,
                    deleteWidget = { deleteWidget(widgetInfo) },
                )
            }
        }
    }
}
