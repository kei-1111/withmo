package io.github.kei_1111.withmo.core.data.manager

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.kei_1111.withmo.core.domain.manager.AppUsageStatsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUsageStatsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppUsageStatsManager {
    private val usageStatsManager: UsageStatsManager? =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager

    private val packageManager: PackageManager = context.packageManager

    override fun getAppUsageCounts(): Map<String, Int> {
        val usageStatsManager = this.usageStatsManager ?: return emptyMap()

        return try {
            val startTime = 0L // 端末起動時から（利用可能な最古のデータ）
            val currentTime = System.currentTimeMillis()

            val result = buildLaunchCountMapFromEvents(usageStatsManager, startTime, currentTime)

            result
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get app usage counts", e)
            emptyMap()
        }
    }

    private fun buildLaunchCountMapFromEvents(
        usageStatsManager: UsageStatsManager,
        startTime: Long,
        endTime: Long,
    ): Map<String, Int> {
        val launchableApps = getLaunchableAppPackageNames()
        val counts = launchableApps.associateWith { 0 }.toMutableMap()

        try {
            val events = usageStatsManager.queryEvents(startTime, endTime)
            val event = UsageEvents.Event()

            while (events.hasNextEvent()) {
                events.getNextEvent(event)

                val isLaunch = when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> true // アプリが起動された
                    UsageEvents.Event.MOVE_TO_FOREGROUND -> true // 旧アプリが起動された
                    else -> false
                }

                if (isLaunch && event.packageName in launchableApps) {
                    counts[event.packageName] = (counts[event.packageName] ?: 0) + 1
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to query usage events", e)
        }
        return counts
    }

    // AppUtilsで取得している起動可能なアプリ一覧と同じロジックでアプリ一覧を取得
    private fun getLaunchableAppPackageNames(): Set<String> {
        val launchableIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return packageManager.queryIntentActivities(launchableIntent, 0)
            .filter { resolveInfo ->
                val packageName = resolveInfo.activityInfo.packageName
                packageName != context.packageName &&
                    packageManager.getLaunchIntentForPackage(packageName) != null
            }
            .map { it.activityInfo.packageName }
            .toSet()
    }

    private companion object {
        const val TAG = "AppUsageStatsManager"
    }
}
