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
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.PlacedWidget
import io.github.kei_1111.withmo.core.ui.LocalAppWidgetHost

@Composable
fun Widget(
    placedWidget: PlacedWidget,
    modifier: Modifier = Modifier,
) {
    val appWidgetHost = LocalAppWidgetHost.current
    val context = LocalContext.current

    val hostView = remember(placedWidget.info.id) {
        appWidgetHost.createView(
            context.applicationContext,
            placedWidget.info.id,
            placedWidget.info.info,
        ).apply {
            val widgetSizeBundle = Bundle().apply {
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, placedWidget.width)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, placedWidget.height)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, placedWidget.width)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, placedWidget.height)
            }

            updateAppWidgetSize(
                widgetSizeBundle,
                placedWidget.width,
                placedWidget.height,
                placedWidget.width,
                placedWidget.height,
            )
        }
    }

    AndroidView(
        factory = { hostView },
        modifier = modifier
            .size(placedWidget.width.dp, placedWidget.height.dp)
            .padding(Paddings.Small)
            .clip(MaterialTheme.shapes.medium),
        update = { view ->
            view.setPadding(0, 0, 0, 0)
        },
    )
}
