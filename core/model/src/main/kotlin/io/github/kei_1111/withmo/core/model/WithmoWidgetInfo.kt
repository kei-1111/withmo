package io.github.kei_1111.withmo.core.model

import android.appwidget.AppWidgetProviderInfo
import androidx.compose.ui.geometry.Offset

data class WithmoWidgetInfo(
    val info: WidgetInfo,
    var width: Int = 0,
    var height: Int = 0,
    override var position: Offset = Offset.Zero,
) : PlaceableItem {
    override val id: String
        get() = info.id.toString()
}

data class WidgetInfo(
    val id: Int,
    val info: AppWidgetProviderInfo,
)
