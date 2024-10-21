package com.example.withmo.ui.component

import android.content.Context
import android.view.View
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Suppress("LongMethod")
@Composable
fun WithmoWidget(
    widgetInfo: WidgetInfo,
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    isEditMode: Boolean,
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

    val widgetWidth = widgetInfo.info.minWidth
    val widgetHeight = widgetInfo.info.minHeight

    val scaledWidgetWidth = (widgetWidth * scale).roundToInt()
    val scaledWidgetHeight = (widgetHeight * scale).roundToInt()

    val draggedSpaceWidth = screenWidth - startPadding.value.roundToInt() - endPadding.value.roundToInt()
    val draggedSpaceHeight = screenHeight - topPadding.value.roundToInt() - bottomPadding.value.roundToInt()

    val adjustWidgetWidth = if (scaledWidgetWidth > draggedSpaceWidth) {
        draggedSpaceWidth
    } else {
        scaledWidgetWidth
    }
    val adjustWidgetHeight = if (scaledWidgetHeight > draggedSpaceHeight) {
        draggedSpaceHeight
    } else {
        scaledWidgetHeight
    }

    val adjustWidgetWidthPx = with(density) { adjustWidgetWidth.dp.toPx() }
    val adjustWidgetHeightPx = with(density) { adjustWidgetHeight.dp.toPx() }

    val widgetModifier = if (isEditMode) {
        modifier
            .width(adjustWidgetWidth.dp)
            .height(adjustWidgetHeight.dp)
            .offset {
                IntOffset(position.x.roundToInt(), position.y.roundToInt())
            }
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
            .width(adjustWidgetWidth.dp)
            .height(adjustWidgetHeight.dp)
            .offset {
                IntOffset(position.x.roundToInt(), position.y.roundToInt())
            }
    }

    AndroidView(
        factory = { context ->
            createWidgetView(context.applicationContext, widgetInfo, adjustWidgetWidth, adjustWidgetHeight)
        },
        modifier = widgetModifier,
    )
}
