package com.example.withmo.until

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.withmo.R
import com.example.withmo.domain.model.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

fun getAppList(context: Context): ImmutableList<AppInfo> {
    val pm = context.packageManager

    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    val launchableApps = pm.queryIntentActivities(intent, 0)

    return launchableApps
        .filter {
            it.activityInfo.packageName != context.packageName
            pm.getLaunchIntentForPackage(it.activityInfo.packageName) != null
        }
        .map {
            AppInfo(
                icon = it.loadIcon(pm) ?: context.resources.getDrawable(R.mipmap.ic_launcher, null),
                label = if (it.activityInfo.packageName == context.packageName) {
                    "withmoの設定"
                } else {
                    it.loadLabel(pm).toString()
                },
                packageName = it.activityInfo.packageName,
            )
        }
        .sortedBy { it.label }
        .toPersistentList()
}
