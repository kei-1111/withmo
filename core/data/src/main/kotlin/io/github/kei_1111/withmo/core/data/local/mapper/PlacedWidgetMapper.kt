package io.github.kei_1111.withmo.core.data.local.mapper

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.util.Log
import androidx.compose.ui.geometry.Offset
import io.github.kei_1111.withmo.core.data.local.entity.PlacedWidgetEntity
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo

fun PlacedWidgetInfo.toEntity(): PlacedWidgetEntity {
    return PlacedWidgetEntity(
        id = info.id,
        appWidgetProviderClassName = info.info.provider.className,
        width = width,
        height = height,
        positionX = position.x,
        positionY = position.y,
    )
}

fun PlacedWidgetEntity.toPlacedWidgetInfo(appWidgetManager: AppWidgetManager): PlacedWidgetInfo? {
    val info = getAppWidgetProviderInfo(appWidgetManager, appWidgetProviderClassName)
    if (info == null) {
        Log.e("toWidgetInfo", "AppWidgetProviderInfo not found: $appWidgetProviderClassName")
        return null
    }

    return PlacedWidgetInfo(
        info = WidgetInfo(id, info),
        width = width,
        height = height,
        position = Offset(positionX, positionY),
    )
}

private fun getAppWidgetProviderInfo(
    appWidgetManager: AppWidgetManager,
    appWidgetProviderClassName: String,
): AppWidgetProviderInfo? {
    val installedProviders = appWidgetManager.installedProviders

    return installedProviders.firstOrNull { it.provider.className == appWidgetProviderClassName }
}
