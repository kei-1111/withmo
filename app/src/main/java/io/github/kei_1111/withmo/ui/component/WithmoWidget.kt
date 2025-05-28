package io.github.kei_1111.withmo.ui.component

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ZoomOutMap
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.viewinterop.AndroidView
import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.core.ui.LocalAppWidgetHost
import io.github.kei_1111.withmo.core.util.ktx.toDp
import io.github.kei_1111.withmo.core.util.ktx.toInt
import io.github.kei_1111.withmo.core.util.ktx.toPx
import io.github.kei_1111.withmo.ui.theme.dimensions.BadgeSizes
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import kotlin.math.roundToInt

private val BorderWidth = 1.dp

@Suppress("LongMethod")
@Composable
fun WithmoWidget(
    withmoWidgetInfo: WithmoWidgetInfo,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    deleteWidget: () -> Unit = {},
    resizeWidget: () -> Unit = {},
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

    val draggedSpaceWidth = screenWidth.dp - startPadding - endPadding
    val draggedSpaceHeight = screenHeight.dp - topPadding - bottomPadding
    val minDraggedSpaceDimension = min(draggedSpaceWidth, draggedSpaceHeight)

    if (withmoWidgetInfo.width == 0) {
        withmoWidgetInfo.width = calculateWidgetWidth(
            widgetWidth = withmoWidgetInfo.widgetInfo.info.minWidth.toDp(),
            minDraggedSpaceDimension = minDraggedSpaceDimension,
        )
    }

    if (withmoWidgetInfo.height == 0) {
        withmoWidgetInfo.height = calculateWidgetHeight(
            widgetHeight = withmoWidgetInfo.widgetInfo.info.minHeight.toDp(),
            minDraggedSpaceDimension = screenWidth.dp - startPadding - endPadding,
        )
    }

    val widgetWidthPx = withmoWidgetInfo.width.toPx()
    val widgetHeightPx = withmoWidgetInfo.height.toPx()

    val availableWidthPx = screenWidthPx - endPaddingPx
    val availableHeightPx = screenHeightPx - bottomPaddingPx

    var position by remember {
        mutableStateOf(
            calculateWidgetPosition(
                widgetPosition = withmoWidgetInfo.position,
                widgetWidthPx = widgetWidthPx,
                widgetHeightPx = widgetHeightPx,
                availableWidthPx = availableWidthPx,
                availableHeightPx = availableHeightPx,
                startPaddingPx = startPaddingPx,
                topPaddingPx = topPaddingPx,
            ),
        )
    }

    val widgetModifier = if (isEditMode) {
        modifier
            .width(withmoWidgetInfo.width.dp)
            .height(withmoWidgetInfo.height.dp)
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()

                        val newOffsetX = (position.x + dragAmount.x).coerceIn(
                            startPaddingPx,
                            availableWidthPx - widgetWidthPx,
                        )
                        val newOffsetY = (position.y + dragAmount.y).coerceIn(
                            topPaddingPx,
                            availableHeightPx - widgetHeightPx,
                        )

                        position = Offset(newOffsetX, newOffsetY)
                        withmoWidgetInfo.position = position
                    },
                )
            }
            .border(
                width = BorderWidth,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium,
            )
    } else {
        modifier
            .width(withmoWidgetInfo.width.dp)
            .height(withmoWidgetInfo.height.dp)
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
    }

    Box(
        modifier = widgetModifier,
    ) {
        Widget(
            withmoWidgetInfo = withmoWidgetInfo,
            modifier = Modifier.align(Alignment.Center),
        )
        if (isEditMode) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.medium,
                color = Color.Transparent,
            ) {
                Box(
                    modifier = Modifier
                        .padding(Paddings.ExtraSmall),
                ) {
                    DeleteWidgetBadge(
                        onClick = deleteWidget,
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                    )
                    ResizeWidgetBadge(
                        onClick = resizeWidget,
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteWidgetBadge(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WidgetBadge(
        icon = Icons.Rounded.Close,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
private fun ResizeWidgetBadge(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WidgetBadge(
        icon = Icons.Rounded.ZoomOutMap,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
private fun WidgetBadge(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(BadgeSizes.Large),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(Paddings.ExtraSmall),
        )
    }
}

@Composable
fun Widget(
    withmoWidgetInfo: WithmoWidgetInfo,
    modifier: Modifier = Modifier,
) {
    val appWidgetHost = LocalAppWidgetHost.current
    val context = LocalContext.current

    val hostView = remember(withmoWidgetInfo.widgetInfo.id) {
        appWidgetHost.createView(
            context.applicationContext,
            withmoWidgetInfo.widgetInfo.id,
            withmoWidgetInfo.widgetInfo.info,
        ).apply {
            val widgetSizeBundle = Bundle().apply {
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, withmoWidgetInfo.width)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, withmoWidgetInfo.height)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, withmoWidgetInfo.width)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, withmoWidgetInfo.height)
            }

            updateAppWidgetSize(
                widgetSizeBundle,
                withmoWidgetInfo.width,
                withmoWidgetInfo.height,
                withmoWidgetInfo.width,
                withmoWidgetInfo.height,
            )
        }
    }

    AndroidView(
        factory = { hostView },
        modifier = modifier
            .size(withmoWidgetInfo.width.dp, withmoWidgetInfo.height.dp),
    )
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

fun calculateWidgetPosition(
    widgetPosition: Offset,
    widgetWidthPx: Float,
    widgetHeightPx: Float,
    availableWidthPx: Float,
    availableHeightPx: Float,
    startPaddingPx: Float,
    topPaddingPx: Float,
): Offset {
    var x = widgetPosition.x.coerceAtLeast(startPaddingPx)
    var y = widgetPosition.y.coerceAtLeast(topPaddingPx)

    if (widgetPosition.x + widgetWidthPx > availableWidthPx) {
        x = (availableWidthPx - widgetWidthPx).coerceAtLeast(startPaddingPx)
    }
    if (widgetPosition.y + widgetHeightPx > availableHeightPx) {
        y = (availableHeightPx - widgetHeightPx).coerceAtLeast(topPaddingPx)
    }

    return Offset(x, y)
}
