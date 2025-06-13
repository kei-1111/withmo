package io.github.kei_1111.withmo.core.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import io.github.kei_1111.withmo.core.common.IntentConstants

private const val AppInfoDefaultUseCount = 0

/**
 * withmoで使用するアプリ情報を表すクラス
 * 基本的なアプリ情報を表すinfoと、ホーム画面上に配置したときの位置情報、お気に入り順を持つ
 */
data class WithmoAppInfo(
    val info: AppInfo,
    val favoriteOrder: FavoriteOrder = FavoriteOrder.NotFavorite,
    override var position: Offset = Offset.Unspecified,
) : PlaceableItem {
    override val id: String
        get() = info.packageName
}

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
                val startActivityIntent = Intent(IntentConstants.Action.StartActivity).apply {
                    putExtra(IntentConstants.ExtraKey.PackageName, packageName)
                    setPackage(context.packageName)
                }
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

enum class FavoriteOrder {
    NotFavorite,
    First,
    Second,
    Third,
    Fourth,
}
