package io.github.kei_1111.withmo.data.mapper

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteOrder
import io.github.kei_1111.withmo.core.util.AppUtils
import io.github.kei_1111.withmo.data.local.entity.AppInfoEntity

fun AppInfo.toEntity(): AppInfoEntity {
    return AppInfoEntity(
        packageName = packageName,
        notification = notification,
        useCount = useCount,
        favoriteOrder = favoriteOrder.name,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun AppInfoEntity.toAppInfo(context: Context): AppInfo? {
    val info = AppUtils.getAppFromPackageName(context, packageName)
    if (info == null) {
        Log.e("toAppInfo", "AppInfo not found: $packageName")
        return null
    }

    return AppInfo(
        appIcon = info.appIcon,
        label = info.label,
        packageName = packageName,
        notification = notification,
        useCount = useCount,
        favoriteOrder = FavoriteOrder.valueOf(favoriteOrder),
    )
}
