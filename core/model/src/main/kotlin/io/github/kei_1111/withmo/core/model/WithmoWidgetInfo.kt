package io.github.kei_1111.withmo.core.model

import android.appwidget.AppWidgetProviderInfo
import androidx.compose.ui.geometry.Offset

data class WithmoWidgetInfo(
    val widgetInfo: WidgetInfo,
    var position: Offset = Offset.Zero,
    var width: Int = 0,
    var height: Int = 0,
)

data class WidgetInfo(
    val id: Int,
    val info: AppWidgetProviderInfo,
)
