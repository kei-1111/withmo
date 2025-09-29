package io.github.kei_1111.withmo.core.designsystem.component

import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.util.ktx.toPx
import kotlin.math.roundToInt

private val BorderWidth = 1.dp

// startとtopにPaddingがついていると、そのPadding分初期位置がズレてめり込んでしまうため、Paddingのついていないようにする
@Suppress("LongMethod", "ComposableParamOrder")
@Composable
fun PlaceableItemContainer(
    placeableItem: PlaceableItem,
    width: Dp,
    height: Dp,
    topPaddingPx: Float,
    bottomPaddingPx: Float,
    startPaddingPx: Float,
    endPaddingPx: Float,
    isEditMode: Boolean,
    modifier: Modifier = Modifier,
    onDeleteBadgeClick: () -> Unit,
    onResizeBadgeClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current

    val widthPx = width.toPx()
    val heightPx = height.toPx()

    val screenWidthPx = configuration.screenWidthDp.toPx()
    val screenHeightPx = configuration.screenHeightDp.toPx()

    val systemBarHeight = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
    val navigationBarHeight = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

    // PlaceableItemを動かすことができるOffsetXの最大値を計算
    val maxOffsetX = screenWidthPx - endPaddingPx - widthPx
    // PlaceableItemを動かすことができるOffsetYの最大値を計算
    val maxOffsetY = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        // Android 15以下ではscreenWidthで取得できるのが画面全体のサイズではない。
        // safeDrawingのPaddingが勝手に引かれてしまうため、Paddingを考慮して計算する。
        // ↑に関する記事 https://developer.android.com/about/versions/15/behavior-changes-15?hl=ja
        screenHeightPx - bottomPaddingPx - heightPx + systemBarHeight.toPx() + navigationBarHeight.toPx()
    } else {
        screenHeightPx - bottomPaddingPx - heightPx
    }

    var position by remember {
        mutableStateOf(
            calculatePlaceableItemPosition(
                position = placeableItem.position,
                maxOffsetX = maxOffsetX,
                maxOffsetY = maxOffsetY,
                startPaddingPx = startPaddingPx,
                topPaddingPx = topPaddingPx,
            ),
        )
    }

    val placeableItemModifier = modifier
        .width(width)
        .height(height)
        .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
        .then(
            if (isEditMode) {
                Modifier
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()

                                val newOffsetX = (position.x + dragAmount.x).coerceIn(
                                    startPaddingPx,
                                    maxOffsetX,
                                )
                                val newOffsetY = (position.y + dragAmount.y).coerceIn(
                                    topPaddingPx,
                                    maxOffsetY,
                                )

                                position = Offset(newOffsetX, newOffsetY)
                                placeableItem.position = position
                            },
                        )
                    }
                    .border(
                        width = BorderWidth,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium,
                    )
            } else {
                Modifier
            },
        )

    Box(
        modifier = placeableItemModifier,
        contentAlignment = Alignment.Center,
    ) {
        content()
        if (isEditMode) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.medium,
                color = Color.Transparent,
            ) {
                Box(
                    modifier = Modifier.padding(4.dp),
                ) {
                    DeleteBadge(
                        onClick = onDeleteBadgeClick,
                        modifier = Modifier
                            .align(Alignment.TopStart),
                    )
                    onResizeBadgeClick?.let {
                        ResizeBadge(
                            onClick = onResizeBadgeClick,
                            modifier = Modifier
                                .align(Alignment.BottomEnd),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteBadge(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PlaceableItemBadge(
        icon = Icons.Rounded.Close,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
private fun ResizeBadge(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PlaceableItemBadge(
        icon = Icons.Rounded.ZoomOutMap,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
private fun PlaceableItemBadge(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(25.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(4.dp),
        )
    }
}

fun calculatePlaceableItemPosition(
    position: Offset,
    maxOffsetX: Float,
    maxOffsetY: Float,
    startPaddingPx: Float,
    topPaddingPx: Float,
): Offset {
    var x = position.x.coerceAtLeast(startPaddingPx)
    var y = position.y.coerceAtLeast(topPaddingPx)

    if (position.x > maxOffsetX) {
        x = maxOffsetX.coerceAtLeast(startPaddingPx)
    }
    if (position.y > maxOffsetY) {
        y = maxOffsetY.coerceAtLeast(topPaddingPx)
    }

    return Offset(x, y)
}

@Preview
@Composable
private fun DeleteWidgetBadgeLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        DeleteBadge(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun DeleteWidgetBadgeDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        DeleteBadge(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun ResizeWidgetBadgeLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ResizeBadge(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun ResizeWidgetBadgeDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ResizeBadge(
            onClick = {},
        )
    }
}
