package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import io.github.kei_1111.withmo.core.model.PlacedWidget
import io.github.kei_1111.withmo.core.util.ktx.toDp
import io.github.kei_1111.withmo.core.util.ktx.toInt
import io.github.kei_1111.withmo.core.util.ktx.toPx

@Suppress("LongMethod")
@Composable
fun WithmoWidget(
    placedWidget: PlacedWidget,
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

    if (placedWidget.width == 0) {
        placedWidget.width = calculateWidgetWidth(
            widgetWidth = placedWidget.info.info.minWidth.toDp(),
            minDraggedSpaceDimension = minDraggedSpaceDimension,
        )
    }

    if (placedWidget.height == 0) {
        placedWidget.height = calculateWidgetHeight(
            widgetHeight = placedWidget.info.info.minHeight.toDp(),
            minDraggedSpaceDimension = minDraggedSpaceDimension,
        )
    }

    PlaceableItemContainer(
        placeableItem = placedWidget,
        width = placedWidget.width.dp,
        height = placedWidget.height.dp,
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
            placedWidget = placedWidget,
        )
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
