package io.github.kei_1111.withmo.core.service.permission

import android.app.AppOpsManager
import android.content.Context
import android.os.Process
import androidx.core.app.NotificationManagerCompat
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import javax.inject.Inject

class PermissionCheckerImpl @Inject constructor(
    private val context: Context,
) : PermissionChecker {
    override fun isNotificationListenerEnabled(): Boolean =
        NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)

    override fun isUsageStatsPermissionGranted(): Boolean {
        val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager
            ?: return false

        val mode = appOpsManager.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName,
        )

        return mode == AppOpsManager.MODE_ALLOWED
    }
}
