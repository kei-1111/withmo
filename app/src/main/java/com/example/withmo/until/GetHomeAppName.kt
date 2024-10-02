package com.example.withmo.until

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

fun getHomeAppName(context: Context): String? {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)

    val resolveInfo: ResolveInfo? =
        context.packageManager.resolveActivity(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY,
        )

    return resolveInfo?.activityInfo?.packageName
}
