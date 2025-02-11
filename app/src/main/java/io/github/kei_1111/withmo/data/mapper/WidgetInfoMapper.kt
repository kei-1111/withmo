package io.github.kei_1111.withmo.data.mapper

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import androidx.compose.ui.geometry.Offset
import io.github.kei_1111.withmo.data.local.entity.WidgetInfoEntity
import io.github.kei_1111.withmo.domain.model.WidgetInfo

fun WidgetInfo.toEntity(): WidgetInfoEntity {
    return WidgetInfoEntity(
        id = id,
        appWidgetProviderClassName = info.provider.className,
        positionX = position.x,
        positionY = position.y,
        width = width,
        height = height,
    )
}

fun WidgetInfoEntity.toWidgetInfo(appWidgetManager: AppWidgetManager): WidgetInfo {
    return WidgetInfo(
        id = id,
        info = getAppWidgetProviderInfo(appWidgetManager, appWidgetProviderClassName)
            ?: throw IllegalArgumentException("AppWidgetProviderInfo not found"),
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
