package io.github.kei_1111.withmo.data.mapper

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.util.Log
import androidx.compose.ui.geometry.Offset
import io.github.kei_1111.withmo.data.local.entity.WidgetInfoEntity
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.model.WithmoWidgetInfo

fun WithmoWidgetInfo.toEntity(): WidgetInfoEntity {
    return WidgetInfoEntity(
        id = widgetInfo.id,
        appWidgetProviderClassName = widgetInfo.info.provider.className,
        positionX = position.x,
        positionY = position.y,
        width = width,
        height = height,
    )
}

fun WidgetInfoEntity.toWidgetInfo(appWidgetManager: AppWidgetManager): WithmoWidgetInfo? {
    val info = getAppWidgetProviderInfo(appWidgetManager, appWidgetProviderClassName)
    if (info == null) {
        Log.e("toWidgetInfo", "AppWidgetProviderInfo not found: $appWidgetProviderClassName")
        return null
    }

    return WithmoWidgetInfo(
        widgetInfo = WidgetInfo(id, info),
        position = Offset(positionX, positionY),
        width = width,
        height = height,
    )
}

private fun getAppWidgetProviderInfo(
    appWidgetManager: AppWidgetManager,
    appWidgetProviderClassName: String,
): AppWidgetProviderInfo? {
    val installedProviders = appWidgetManager.installedProviders

    return installedProviders.firstOrNull { it.provider.className == appWidgetProviderClassName }
}
