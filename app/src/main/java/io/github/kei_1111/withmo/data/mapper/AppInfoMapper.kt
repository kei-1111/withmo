package io.github.kei_1111.withmo.data.mapper

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.data.local.entity.AppInfoEntity
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.FavoriteOrder
import io.github.kei_1111.withmo.utils.AppUtils

fun AppInfo.toEntity(): AppInfoEntity {
    return AppInfoEntity(
        packageName = packageName,
        notification = notification,
        useCount = useCount,
        favoriteOrder = favoriteOrder.name,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun AppInfoEntity.toAppInfo(context: Context): AppInfo {
    val appInfo = AppUtils.getAppFromPackageName(context, packageName)
    return appInfo?.let {
        AppInfo(
            appIcon = it.appIcon,
            label = it.label,
            packageName = packageName,
            notification = notification,
            useCount = useCount,
            favoriteOrder = FavoriteOrder.valueOf(favoriteOrder),
        )
    } ?: throw IllegalArgumentException("AppInfo not found")
}
