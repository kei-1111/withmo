package com.example.withmo.until

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.withmo.domain.model.AppIcon
import com.example.withmo.domain.model.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@RequiresApi(Build.VERSION_CODES.O)
fun getAppList(context: Context): ImmutableList<AppInfo> {
    val pm = context.packageManager

    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    val launchableApps = pm.queryIntentActivities(intent, 0)

    return launchableApps
        .filter {
            val packageName = it.activityInfo.packageName

            packageName != context.packageName
            pm.getLaunchIntentForPackage(packageName) != null
        }
        .map {
            val icon = it.loadIcon(pm)
            val packageName = it.activityInfo.packageName

            AppInfo(
                appIcon = getAppIcon(icon),
                label = if (packageName == context.packageName) {
                    "withmoの設定"
                } else {
                    it.loadLabel(pm).toString()
                },
                packageName = packageName,
            )
        }
        .sortedBy { it.label }
        .toPersistentList()
}

@RequiresApi(Build.VERSION_CODES.O)
fun getAppIcon(icon: Drawable): AppIcon {
    return if (icon is AdaptiveIconDrawable) {
        AppIcon(
            foregroundIcon = icon.foreground,
            backgroundIcon = icon.background,
        )
    } else {
        AppIcon(
            foregroundIcon = icon,
            backgroundIcon = null,
        )
    }
}
