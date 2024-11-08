package com.example.withmo.data.mapper

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.withmo.data.local.entity.AppInfoEntity
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.FavoriteOrder
import com.example.withmo.utils.AppUtils

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
