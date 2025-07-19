package io.github.kei_1111.withmo.core.service.manager.app

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("TooManyFunctions")
@Singleton
class AppManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val packageManager: PackageManager = context.packageManager
    private val usageStatsManager: UsageStatsManager? =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager

    private val _appInfoList = MutableStateFlow<List<AppInfo>>(emptyList())
    override val appInfoList: StateFlow<List<AppInfo>> = _appInfoList.asStateFlow()

    init {
        scope.launch {
            refreshAppList()
        }
    }

    override suspend fun refreshAppList(): Unit = withContext(Dispatchers.IO) {
        try {
            val baseAppList = getAppList()
            val usageCounts = getAppUsageCounts()

            val updatedList = baseAppList.map { appInfo ->
                appInfo.copy(
                    useCount = usageCounts[appInfo.packageName] ?: 0,
                )
            }

            _appInfoList.value = updatedList
        } catch (e: Exception) {
            Log.e(TAG, "Failed to refresh app list", e)
        }
    }

    override fun updateNotifications(notificationMap: Map<String, Boolean>) {
        scope.launch {
            try {
                val currentList = _appInfoList.value
                val updatedList = currentList.map { appInfo ->
                    appInfo.copy(
                        notification = notificationMap[appInfo.packageName] ?: false,
                    )
                }
                _appInfoList.value = updatedList
            } catch (e: Exception) {
                Log.e(TAG, "Failed to update notifications", e)
            }
        }
    }

    override suspend fun updateUsageCounts(): Unit = withContext(Dispatchers.IO) {
        try {
            val currentList = _appInfoList.value
            val usageCounts = getAppUsageCounts()

            val updatedList = currentList.map { appInfo ->
                appInfo.copy(
                    useCount = usageCounts[appInfo.packageName] ?: 0,
                )
            }

            _appInfoList.value = updatedList
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update usage counts", e)
        }
    }

    // AppUtilsから移植
    private fun getAppList(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val launchableApps = packageManager.queryIntentActivities(intent, 0)

        return launchableApps
            .filter {
                val packageName = it.activityInfo.packageName
                packageName != context.packageName &&
                    packageManager.getLaunchIntentForPackage(packageName) != null
            }
            .map {
                val icon = it.loadIcon(packageManager)
                val packageName = it.activityInfo.packageName

                AppInfo(
                    appIcon = getAppIcon(icon),
                    label = if (packageName == context.packageName) {
                        "カスタマイズ"
                    } else {
                        it.loadLabel(packageManager).toString()
                    },
                    packageName = packageName,
                )
            }
            .sortedBy { it.label }
    }

    private fun getAppFromPackageName(packageName: String): AppInfo? {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            val icon = packageManager.getApplicationIcon(appInfo)
            val label = packageManager.getApplicationLabel(appInfo).toString()

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
            Log.e(TAG, "Failed to get app info for package: $packageName", e)
            null
        }
    }

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

    // AppUsageStatsManagerから移植
    private fun getAppUsageCounts(): Map<String, Int> {
        val usageStatsManager = this.usageStatsManager ?: return emptyMap()

        return try {
            val startTime = 0L // 端末起動時から（利用可能な最古のデータ）
            val currentTime = System.currentTimeMillis()

            buildLaunchCountMapFromEvents(usageStatsManager, startTime, currentTime)
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
                    UsageEvents.Event.ACTIVITY_RESUMED -> true
                    UsageEvents.Event.MOVE_TO_FOREGROUND -> true
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

    fun getHomeAppName(): String? {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)

        val resolveInfo: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY,
            )

        return resolveInfo?.activityInfo?.packageName
    }

    fun isSystemApp(packageName: String): Boolean {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Failed to get ApplicationInfo for package: $packageName", e)
            false
        }
    }

    fun isProtectedApp(packageName: String): Boolean {
        val protectedPackages = setOf(
            context.packageName,
        )

        return protectedPackages.contains(packageName)
    }

    private companion object {
        const val TAG = "AppManager"
    }
}
