package io.github.kei_1111.withmo.core.designsystem.component

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.ui.LocalAppWidgetHost

@Composable
fun Widget(
    placedWidgetInfo: PlacedWidgetInfo,
    modifier: Modifier = Modifier,
) {
    val appWidgetHost = LocalAppWidgetHost.current
    val context = LocalContext.current

    val hostView = remember(placedWidgetInfo.info.id) {
        appWidgetHost.createView(
            context.applicationContext,
            placedWidgetInfo.info.id,
            placedWidgetInfo.info.info,
        ).apply {
            val widgetSizeBundle = Bundle().apply {
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, placedWidgetInfo.width)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, placedWidgetInfo.height)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, placedWidgetInfo.width)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, placedWidgetInfo.height)
            }

            updateAppWidgetSize(
                widgetSizeBundle,
                placedWidgetInfo.width,
                placedWidgetInfo.height,
                placedWidgetInfo.width,
                placedWidgetInfo.height,
            )
        }
    }

    AndroidView(
        factory = { hostView },
        modifier = modifier
            .size(placedWidgetInfo.width.dp, placedWidgetInfo.height.dp)
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium),
        update = { view ->
            view.setPadding(0, 0, 0, 0)
        },
    )
}
