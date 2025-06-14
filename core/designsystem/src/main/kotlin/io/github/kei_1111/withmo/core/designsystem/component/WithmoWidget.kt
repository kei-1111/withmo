package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.core.util.ktx.toDp
import io.github.kei_1111.withmo.core.util.ktx.toInt
import io.github.kei_1111.withmo.core.util.ktx.toPx

@Suppress("LongMethod")
@Composable
fun WithmoWidget(
    withmoWidgetInfo: WithmoWidgetInfo,
    topPadding: Dp,
    bottomPadding: Dp,
    startPadding: Dp,
    endPadding: Dp,
    isEditMode: Boolean,
    deleteWidget: () -> Unit,
    resizeWidget: () -> Unit,
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

    if (withmoWidgetInfo.width == 0) {
        withmoWidgetInfo.width = calculateWidgetWidth(
            widgetWidth = withmoWidgetInfo.widgetInfo.info.minWidth.toDp(),
            minDraggedSpaceDimension = minDraggedSpaceDimension,
        )
    }

    if (withmoWidgetInfo.height == 0) {
        withmoWidgetInfo.height = calculateWidgetHeight(
            widgetHeight = withmoWidgetInfo.widgetInfo.info.minHeight.toDp(),
            minDraggedSpaceDimension = minDraggedSpaceDimension,
        )
    }

    PlaceableItemContainer(
        placeableItem = withmoWidgetInfo,
        width = withmoWidgetInfo.width.dp,
        height = withmoWidgetInfo.height.dp,
        topPaddingPx = topPadding.toPx(),
        bottomPaddingPx = bottomPadding.toPx(),
        startPaddingPx = startPadding.toPx(),
        endPaddingPx = endPadding.toPx(),
        isEditMode = isEditMode,
        modifier = modifier,
        deletePlaceableItem = deleteWidget,
        resizePlaceableItem = resizeWidget,
    ) {
        Widget(
            withmoWidgetInfo = withmoWidgetInfo,
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
