package com.example.withmo.domain.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Stable
import com.unity3d.player.s

@Stable
data class AppInfo(
    val icon: Drawable,
    val label: String,
    val packageName: String,
    var notification: Boolean = false,
    var useCount: Int = 0
) {
    fun launch(context: Context) {
        try {
            val pm = context.packageManager
            val intent = pm.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                if (notification) notification = false
                useCount++
                Log.d("launchApp: $packageName", "useCount: $useCount" )
                val startActivityIntent = Intent("start_activity")
                startActivityIntent.putExtra("package_name", packageName)
                context.sendBroadcast(startActivityIntent)
                context.startActivity(intent)
            } else {
                Log.e("LauncherTest", "launchApp: intent is null")
            }
        } catch (e: Exception) {
            Log.e("LauncherTest", "launchApp: ${e.message}")
        }
    }

    fun receiveNotification() {
        notification = true
    }

    fun delete(context: Context) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        context.startActivity(intent)
    }
}