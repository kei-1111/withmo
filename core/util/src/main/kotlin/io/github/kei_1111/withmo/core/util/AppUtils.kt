package io.github.kei_1111.withmo.core.util

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

object AppUtils {

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
                        "カスタマイズ"
                    } else {
                        it.loadLabel(pm).toString()
                    },
                    packageName = packageName,
                )
            }
            .sortedBy { it.label }
            .toPersistentList()
    }

    fun getAppFromPackageName(context: Context, packageName: String): AppInfo? {
        val pm = context.packageManager
        return try {
            val appInfo = pm.getApplicationInfo(packageName, 0)
            val icon = pm.getApplicationIcon(appInfo)
            val label = pm.getApplicationLabel(appInfo).toString()

            AppInfo(
                appIcon = getAppIcon(icon),
                label = if (packageName == context.packageName) {
                    "カスタマイズ"
                } else {
                    label
                },
                packageName = packageName,
            )
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Failed to get app info", e)
            null
        }
    }

    /**
     * 与えられた [Drawable] からアプリ用アイコンを組み立てて返す。
     *
     * ### 背景
     * - Android 8.0 (API 26) 以降は “Adaptive Icon” が導入され、
     *   `Drawable` が `AdaptiveIconDrawable` の場合は
     *   **foreground / background** の 2 レイヤーを持つ。
     * - しかし端末メーカー独自のアイコンパックや旧形式の APK を
     *   OS がラップしたケースでは、`foreground` / `background` の
     *   どちらか一方、または両方が **null** になることがある。
     * - Kotlin ではプラットフォーム型 (`Drawable!`) を非 null として
     *   受け取ると自動挿入された null チェックで
     *   `NullPointerException` が発生するため、ここで安全に判定している。
     *
     * ### 振る舞い
     * 1. `AdaptiveIconDrawable` なら各レイヤーを取得。
     * 2. **foreground が null でなければ** 2 レイヤー構成として返す。
     * 3. foreground が null（＝実質レガシーアイコン）なら
     *    元の `Drawable` を 1 枚絵としてフォールバック。
     *
     * @param icon アプリから取得したアイコン `Drawable`
     * @return [AppIcon] - 分離された 2 レイヤー、またはレガシー 1 レイヤー
     */

    private fun getAppIcon(icon: Drawable): AppIcon {
        val adaptive = icon as? AdaptiveIconDrawable
        val foregroundIcon: Drawable? = adaptive?.foreground
        val backgroundIcon: Drawable? = adaptive?.background

        return if (foregroundIcon != null) {
            AppIcon(
                foregroundIcon = foregroundIcon,
                backgroundIcon = backgroundIcon,
            )
        } else {
            AppIcon(
                foregroundIcon = icon,
                backgroundIcon = null,
            )
        }
    }

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

    fun isSystemApp(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        return try {
            val appInfo = pm.getApplicationInfo(packageName, 0)
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Failed to get ApplicationInfo for package: $packageName", e)
            false
        }
    }

    fun isProtectedApp(context: Context, packageName: String): Boolean {
        val protectedPackages = setOf(
            context.packageName,
        )

        return protectedPackages.contains(packageName)
    }

    private const val TAG = "AppUtils"
}
