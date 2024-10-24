package com.example.withmo.domain.model

import android.appwidget.AppWidgetProviderInfo
import androidx.compose.ui.geometry.Offset

data class WidgetInfo(
    val id: Int,
    val info: AppWidgetProviderInfo,
    var position: Offset = Offset.Zero,
    var width: Int = 0,
    var height: Int = 0,
)
