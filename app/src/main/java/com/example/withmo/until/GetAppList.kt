package com.example.withmo.until

import android.content.Context
import android.content.pm.PackageManager
import com.example.withmo.R
import com.example.withmo.domain.model.AppInfo

fun getAppList(context: Context): List<AppInfo> {
    val pm = context.packageManager
    val flags = PackageManager.MATCH_UNINSTALLED_PACKAGES
    return pm.getInstalledApplications(flags)
        .filter {
            it.packageName != context.packageName
            pm.getLaunchIntentForPackage(it.packageName) != null
        }
        .map {
            AppInfo(
                icon = it.loadIcon(pm) ?: context.resources.getDrawable(R.mipmap.ic_launcher, null),
                label = if (it.packageName == context.packageName) {
                    "withmoの設定"
                } else {
                    it.loadLabel(
                        pm,
                    ).toString()
                },
                packageName = it.packageName,
            )
        }
        .sortedBy { it.label }
        .toList()
}
