package io.github.kei_1111.withmo.feature.home.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import io.github.kei_1111.withmo.core.designsystem.component.Widget
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSlider
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.feature.home.preview.HomeDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.home.preview.HomeLightPreviewEnvironment
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
internal fun WidgetResizeBottomSheet(
    placedWidgetInfo: PlacedWidgetInfo,
    close: (PlacedWidgetInfo) -> Unit,
) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val draggedSpaceWidth = screenWidth.dp - Paddings.Medium - Paddings.Medium
    val draggedSpaceHeight = screenHeight.dp - Paddings.Medium - Paddings.Medium
    val minDraggedSpaceDimension = min(draggedSpaceWidth, draggedSpaceHeight)

    var widgetWidth by remember { mutableFloatStateOf(placedWidgetInfo.width.toFloat()) }
    var widgetHeight by remember { mutableFloatStateOf(placedWidgetInfo.height.toFloat()) }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            close(
                placedWidgetInfo.copy(
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
            Box(
                modifier = Modifier
                    .width(widgetWidth.dp)
                    .height(widgetHeight.dp),
            ) {
                Widget(
                    placedWidgetInfo = placedWidgetInfo.copy(
                        width = widgetWidth.roundToInt(),
                        height = widgetHeight.roundToInt(),
                    ),
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent,
                ) { }
            }
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

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun WidgetResizeBottomSheetLightPreview() {
    HomeLightPreviewEnvironment {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
        ) {
            WithmoSettingItemWithSlider(
                title = "Widget 幅",
                value = 200f,
                onValueChange = {},
                valueRange = 100f..400f,
                modifier = Modifier.fillMaxWidth(),
                steps = 1,
            )
            WithmoSettingItemWithSlider(
                title = "Widget 高さ",
                value = 150f,
                onValueChange = {},
                valueRange = 100f..400f,
                modifier = Modifier.fillMaxWidth(),
                steps = 1,
            )
        }
    }
}

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun WidgetResizeBottomSheetDarkPreview() {
    HomeDarkPreviewEnvironment {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
        ) {
            WithmoSettingItemWithSlider(
                title = "Widget 幅",
                value = 200f,
                onValueChange = {},
                valueRange = 100f..400f,
                modifier = Modifier.fillMaxWidth(),
                steps = 1,
            )
            WithmoSettingItemWithSlider(
                title = "Widget 高さ",
                value = 150f,
                onValueChange = {},
                valueRange = 100f..400f,
                modifier = Modifier.fillMaxWidth(),
                steps = 1,
            )
        }
    }
}
