package io.github.kei_1111.withmo.core.data.local.mapper

import io.github.kei_1111.withmo.core.data.local.entity.FavoriteAppEntity
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo

fun FavoriteAppInfo.toEntity(): FavoriteAppEntity {
    return FavoriteAppEntity(
        packageName = info.packageName,
        favoriteOrder = favoriteOrder,
    )
}

fun FavoriteAppEntity.toFavoriteAppInfo(appInfo: AppInfo): FavoriteAppInfo {
    return FavoriteAppInfo(
        info = appInfo,
        favoriteOrder = favoriteOrder,
    )
}
