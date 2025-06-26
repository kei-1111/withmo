package io.github.kei_1111.withmo.core.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Stable
import io.github.kei_1111.withmo.core.common.IntentConstants

private const val AppInfoDefaultUseCount = 0

/**
 * 基本的なアプリ情報を表すための最低限のクラス
 */
@Stable
data class AppInfo(
    val appIcon: AppIcon,
    val label: String,
    val packageName: String,
    val notification: Boolean = false,
    val useCount: Int = AppInfoDefaultUseCount,
) {
    fun launch(context: Context) {
        try {
            val pm = context.packageManager
            val intent = pm.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                val startActivityIntent = Intent(IntentConstants.Action.StartActivity)
                context.sendBroadcast(startActivityIntent)
                context.startActivity(intent)
            } else {
                Log.e("LauncherTest", "launchApp: intent is null")
            }
        } catch (e: Exception) {
            Log.e("LauncherTest", "launchApp: ${e.message}")
        }
    }

    fun delete(context: Context) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        context.startActivity(intent)
    }
}

data class AppIcon(
    val foregroundIcon: Drawable,
    val backgroundIcon: Drawable?,
)
