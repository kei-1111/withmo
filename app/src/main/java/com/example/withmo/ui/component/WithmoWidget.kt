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
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.viewinterop.AndroidView
import com.example.withmo.domain.model.WidgetInfo
import com.example.withmo.ktx.toDp
import com.example.withmo.ktx.toInt
import com.example.withmo.ktx.toPx
import com.example.withmo.ui.theme.UiConfig
import kotlin.math.roundToInt

@Suppress("LongMethod")
@Composable
fun WithmoWidget(
    widgetInfo: WidgetInfo,
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    isEditMode: Boolean,
    deleteWidget: () -> Unit,
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    bottomPadding: Dp = 0.dp,
    startPadding: Dp = 0.dp,
    endPadding: Dp = 0.dp,
) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val screenWidthPx = screenWidth.toPx()
    val screenHeightPx = screenHeight.toPx()

    val topPaddingPx = topPadding.toPx()
    val bottomPaddingPx = bottomPadding.toPx()
    val startPaddingPx = startPadding.toPx()
    val endPaddingPx = endPadding.toPx()

    var position by remember {
        mutableStateOf(
            Offset(
                x = widgetInfo.position.x.coerceAtLeast(startPaddingPx),
                y = widgetInfo.position.y.coerceAtLeast(topPaddingPx),
            ),
        )
    }

    val draggedSpaceWidth = screenWidth.dp - startPadding - endPadding
    val draggedSpaceHeight = screenHeight.dp - topPadding - bottomPadding
    val minDraggedSpaceDimension = min(draggedSpaceWidth, draggedSpaceHeight)

    if (widgetInfo.width == 0) {
        widgetInfo.width = calculateWidgetWidth(
            widgetWidth = widgetInfo.info.minWidth.toDp(),
            minDraggedSpaceDimension = minDraggedSpaceDimension,
        )
    }

    if (widgetInfo.height == 0) {
        widgetInfo.height = calculateWidgetHeight(
            widgetHeight = widgetInfo.info.minHeight.toDp(),
            minDraggedSpaceDimension = screenWidth.dp - startPadding - endPadding,
        )
    }

    val widgetWidthPx = widgetInfo.width.toPx()
    val widgetHeightPx = widgetInfo.height.toPx()

    val widgetModifier = if (isEditMode) {
        modifier
            .width(widgetInfo.width.dp)
            .height(widgetInfo.height.dp)
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()

                        val newOffsetX = (position.x + dragAmount.x).coerceIn(
                            startPaddingPx,
                            (screenWidthPx - endPaddingPx) - widgetWidthPx,
                        )
                        val newOffsetY = (position.y + dragAmount.y).coerceIn(
                            topPaddingPx,
                            (screenHeightPx - bottomPaddingPx) - widgetHeightPx,
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
                imageVector = Icons.Rounded.Close,
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

private const val SizeDivisor = 3

fun calculateWidgetWidth(
    widgetWidth: Dp,
    minDraggedSpaceDimension: Dp,
): Int {
    return when {
        widgetWidth <= minDraggedSpaceDimension / SizeDivisor -> {
            (minDraggedSpaceDimension / SizeDivisor).toInt()
        }
        widgetWidth <= minDraggedSpaceDimension / SizeDivisor * 2 -> {
            (minDraggedSpaceDimension / SizeDivisor * 2).toInt()
        }
        else -> {
            minDraggedSpaceDimension.toInt()
        }
    }
}

fun calculateWidgetHeight(
    widgetHeight: Dp,
    minDraggedSpaceDimension: Dp,
): Int {
    return when {
        widgetHeight <= minDraggedSpaceDimension / SizeDivisor -> {
            (minDraggedSpaceDimension / SizeDivisor).toInt()
        }
        widgetHeight <= minDraggedSpaceDimension / SizeDivisor * 2 -> {
            (minDraggedSpaceDimension / SizeDivisor * 2).toInt()
        }
        else -> {
            minDraggedSpaceDimension.toInt()
        }
    }
}
