package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.util.ktx.toDp
import io.github.kei_1111.withmo.core.util.ktx.toInt
import io.github.kei_1111.withmo.core.util.ktx.toPx

@Suppress("LongMethod")
@Composable
fun PlacedWidget(
    placedWidgetInfo: PlacedWidgetInfo,
    topPadding: Dp,
    bottomPadding: Dp,
    startPadding: Dp,
    endPadding: Dp,
    isEditMode: Boolean,
    onDeleteBadgeClick: () -> Unit,
    onResizeBadgeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    // Widgetがいびつな形にならないように、画面上のドラッグ可能なスペースの幅と高さを計算し、小さい方に合わせている
    // これにより、Widgetの幅と高さが画面のドラッグ可能なスペースに収まるように調整される
    val draggedSpaceWidth = screenWidth.dp - startPadding - endPadding
    val draggedSpaceHeight = screenHeight.dp - topPadding - bottomPadding
    val minDraggedSpaceDimension = min(draggedSpaceWidth, draggedSpaceHeight)

    if (placedWidgetInfo.width == 0) {
        placedWidgetInfo.width = calculateWidgetWidth(
            widgetWidth = placedWidgetInfo.info.info.minWidth.toDp(),
            minDraggedSpaceDimension = minDraggedSpaceDimension,
        )
    }

    if (placedWidgetInfo.height == 0) {
        placedWidgetInfo.height = calculateWidgetHeight(
            widgetHeight = placedWidgetInfo.info.info.minHeight.toDp(),
            minDraggedSpaceDimension = minDraggedSpaceDimension,
        )
    }

    PlaceableItemContainer(
        placeableItem = placedWidgetInfo,
        width = placedWidgetInfo.width.dp,
        height = placedWidgetInfo.height.dp,
        topPaddingPx = topPadding.toPx(),
        bottomPaddingPx = bottomPadding.toPx(),
        startPaddingPx = startPadding.toPx(),
        endPaddingPx = endPadding.toPx(),
        isEditMode = isEditMode,
        modifier = modifier,
        onDeleteBadgeClick = onDeleteBadgeClick,
        onResizeBadgeClick = onResizeBadgeClick,
    ) {
        Widget(
            placedWidgetInfo = placedWidgetInfo,
        )
    }
}

private const val SIZE_DIVISOR = 3

fun calculateWidgetWidth(
    widgetWidth: Dp,
    minDraggedSpaceDimension: Dp,
): Int {
    return when {
        widgetWidth <= minDraggedSpaceDimension / SIZE_DIVISOR -> {
            (minDraggedSpaceDimension / SIZE_DIVISOR).toInt()
        }

        widgetWidth <= minDraggedSpaceDimension / SIZE_DIVISOR * 2 -> {
            (minDraggedSpaceDimension / SIZE_DIVISOR * 2).toInt()
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
        widgetHeight <= minDraggedSpaceDimension / SIZE_DIVISOR -> {
            (minDraggedSpaceDimension / SIZE_DIVISOR).toInt()
        }

        widgetHeight <= minDraggedSpaceDimension / SIZE_DIVISOR * 2 -> {
            (minDraggedSpaceDimension / SIZE_DIVISOR * 2).toInt()
        }

        else -> {
            minDraggedSpaceDimension.toInt()
        }
    }
}
