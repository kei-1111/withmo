package com.example.withmo.ui.component

import android.content.Context
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.withmo.domain.model.WidgetInfo
import com.example.withmo.ui.theme.UiConfig
import kotlin.math.roundToInt

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun WithmoWidget(
    widgetInfo: WidgetInfo,
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    isEditMode: Boolean,
    deleteWidget: () -> Unit,
    modifier: Modifier = Modifier,
    scale: Float = 0.6f,
    topPadding: Dp = 0.dp,
    bottomPadding: Dp = 0.dp,
    startPadding: Dp = 0.dp,
    endPadding: Dp = 0.dp,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val topPaddingPx = with(density) { topPadding.toPx() }
    val bottomPaddingPx = with(density) { bottomPadding.toPx() }
    val startPaddingPx = with(density) { startPadding.toPx() }
    val endPaddingPx = with(density) { endPadding.toPx() }

    var position by remember {
        mutableStateOf(
            Offset(
                x = widgetInfo.position.x.coerceAtLeast(startPaddingPx),
                y = widgetInfo.position.y.coerceAtLeast(topPaddingPx),
            ),
        )
    }

    if (widgetInfo.width == 0) {
        val minWidgetWidth = widgetInfo.info.minWidth
        val scaledWidgetWidth = (minWidgetWidth * scale).roundToInt()
        val draggedSpaceWidth = screenWidth - startPadding.value.roundToInt() - endPadding.value.roundToInt()
        val adjustWidgetWidth = if (scaledWidgetWidth > draggedSpaceWidth) {
            draggedSpaceWidth
        } else {
            scaledWidgetWidth
        }

        widgetInfo.width = adjustWidgetWidth
    }

    if (widgetInfo.height == 0) {
        val minWidgetHeight = widgetInfo.info.minHeight
        val scaledWidgetHeight = (minWidgetHeight * scale).roundToInt()
        val draggedSpaceHeight = screenHeight - topPadding.value.roundToInt() - bottomPadding.value.roundToInt()
        val adjustWidgetHeight = if (scaledWidgetHeight > draggedSpaceHeight) {
            draggedSpaceHeight
        } else {
            scaledWidgetHeight
        }

        widgetInfo.height = adjustWidgetHeight
    }

    val adjustWidgetWidthPx = with(density) { widgetInfo.width.dp.toPx() }
    val adjustWidgetHeightPx = with(density) { widgetInfo.height.dp.toPx() }

    val widgetModifier = if (isEditMode) {
        modifier
            .width(widgetInfo.width.dp)
            .height(widgetInfo.height.dp)
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()

                        // 画面内に収める
                        val newOffsetX = (position.x + dragAmount.x).coerceIn(
                            startPaddingPx,
                            (screenWidthPx - endPaddingPx) - adjustWidgetWidthPx,
                        )
                        val newOffsetY = (position.y + dragAmount.y).coerceIn(
                            topPaddingPx,
                            (screenHeightPx - bottomPaddingPx) - adjustWidgetHeightPx,
                        )

                        position = Offset(newOffsetX, newOffsetY)
                        widgetInfo.position = position
                    },
                )
            }
            .border(
                width = UiConfig.BorderWidth,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium,
            )
    } else {
        modifier
            .width(widgetInfo.width.dp)
            .height(widgetInfo.height.dp)
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
    }

    Box(
        modifier = widgetModifier,
    ) {
        AndroidView(
            factory = { context ->
                createWidgetView(context.applicationContext, widgetInfo, widgetInfo.width, widgetInfo.height)
            },
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        )
        if (isEditMode) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .padding(UiConfig.TinyPadding)
                    .align(Alignment.TopEnd)
                    .size(UiConfig.BadgeSize)
                    .clickable { deleteWidget() },
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
