package io.github.kei_1111.withmo.core.data.local.mapper

import androidx.compose.ui.geometry.Offset
import io.github.kei_1111.withmo.core.data.local.entity.PlacedAppEntity
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.PlacedAppInfo

fun PlacedAppInfo.toEntity(): PlacedAppEntity {
    return PlacedAppEntity(
        id = id,
        packageName = info.packageName,
        positionX = position.x,
        positionY = position.y,
    )
}

fun PlacedAppEntity.toPlacedAppInfo(appInfo: AppInfo): PlacedAppInfo {
    return PlacedAppInfo(
        id = id,
        info = appInfo,
        position = Offset(positionX, positionY),
    )
}
