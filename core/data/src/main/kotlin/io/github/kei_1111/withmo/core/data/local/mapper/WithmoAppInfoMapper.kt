package io.github.kei_1111.withmo.core.data.local.mapper

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.geometry.Offset
import io.github.kei_1111.withmo.core.data.local.entity.WithmoAppInfoEntity
import io.github.kei_1111.withmo.core.model.FavoriteOrder
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import io.github.kei_1111.withmo.core.util.AppUtils

fun WithmoAppInfo.toEntity(): WithmoAppInfoEntity {
    return WithmoAppInfoEntity(
        packageName = info.packageName,
        notification = info.notification,
        useCount = info.useCount,
        favoriteOrder = favoriteOrder.name,
        positionX = if (position.x.isNaN()) null else position.x,
        positionY = if (position.y.isNaN()) null else position.y,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun WithmoAppInfoEntity.toAppInfo(context: Context): WithmoAppInfo? {
    val info = AppUtils.getAppFromPackageName(context, packageName)
    if (info == null) {
        Log.e("toAppInfo", "AppInfo not found: $packageName")
        return null
    }

    return WithmoAppInfo(
        info = info.copy(
            notification = notification,
            useCount = useCount,
        ),
        favoriteOrder = FavoriteOrder.valueOf(favoriteOrder),
        position = if (positionX != null && positionY != null) {
            Offset(positionX, positionY)
        } else {
            Offset.Unspecified
        },
    )
}
