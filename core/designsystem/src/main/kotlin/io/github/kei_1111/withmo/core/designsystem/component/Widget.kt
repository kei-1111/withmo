package io.github.kei_1111.withmo.core.designsystem.component

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.core.ui.LocalAppWidgetHost

@Composable
fun Widget(
    withmoWidgetInfo: WithmoWidgetInfo,
    modifier: Modifier = Modifier,
) {
    val appWidgetHost = LocalAppWidgetHost.current
    val context = LocalContext.current

    val hostView = remember(withmoWidgetInfo.info.id) {
        appWidgetHost.createView(
            context.applicationContext,
            withmoWidgetInfo.info.id,
            withmoWidgetInfo.info.info,
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
