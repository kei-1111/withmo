package io.github.kei_1111.withmo.data.permission

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import javax.inject.Inject

class PermissionCheckerImpl @Inject constructor(
    private val context: Context,
) : PermissionChecker {
    override fun isNotificationListenerEnabled(): Boolean =
        NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)
}
