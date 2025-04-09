package io.github.kei_1111.withmo.ui.screens.home.component

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.ui.component.Widget
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSlider
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WidgetResizeBottomSheet(
    widgetInfo: WidgetInfo,
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    close: (WidgetInfo) -> Unit,
) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val draggedSpaceWidth = screenWidth.dp - Paddings.Medium - Paddings.Medium
    val draggedSpaceHeight = screenHeight.dp - Paddings.Medium - Paddings.Medium
    val minDraggedSpaceDimension = min(draggedSpaceWidth, draggedSpaceHeight)

    var widgetWidth by remember { mutableFloatStateOf(widgetInfo.width.toFloat()) }
    var widgetHeight by remember { mutableFloatStateOf(widgetInfo.height.toFloat()) }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            close(
                widgetInfo.copy(
                    width = widgetWidth.roundToInt(),
                    height = widgetHeight.roundToInt(),
                ),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Widget(
                widgetInfo = widgetInfo.copy(
                    width = widgetWidth.roundToInt(),
                    height = widgetHeight.roundToInt(),
                ),
                createWidgetView = createWidgetView,
            )
            Column(
                modifier = Modifier.padding(Paddings.Medium),
                verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
            ) {
                WithmoSettingItemWithSlider(
                    title = "Widget 幅",
                    value = widgetWidth,
                    onValueChange = { widgetWidth = it },
                    valueRange = (minDraggedSpaceDimension / 3f).value..minDraggedSpaceDimension.value,
                    modifier = Modifier.fillMaxWidth(),
                    steps = 1,
                )
                WithmoSettingItemWithSlider(
                    title = "Widget 高さ",
                    value = widgetHeight,
                    onValueChange = { widgetHeight = it },
                    valueRange = (minDraggedSpaceDimension / 3f).value..minDraggedSpaceDimension.value,
                    modifier = Modifier.fillMaxWidth(),
                    steps = 1,
                )
            }
        }
    }
}
