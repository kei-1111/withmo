package io.github.kei_1111.withmo.data.manager

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.os.bundleOf
import io.github.kei_1111.withmo.core.domain.manager.WidgetManager
import javax.inject.Inject

class WidgetManagerImpl @Inject constructor(
    private val context: Context,
    private val appWidgetHost: AppWidgetHost,
    private val appWidgetManager: AppWidgetManager,
) : WidgetManager {

    override fun allocateId() = appWidgetHost.allocateAppWidgetId()

    override fun bindAppWidgetId(
        widgetId: Int,
        provider: ComponentName,
        minWidth: Int,
        minHeight: Int,
    ): Boolean {
        val options = bundleOf(
            AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH to minWidth,
            AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH to minWidth,
            AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT to minHeight,
            AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT to minHeight,
        )
        return appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, provider, options)
    }

    override fun getActivityInfo(provider: ComponentName): ActivityInfo? = try {
        context.packageManager.getActivityInfo(provider, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e(TAG, "Failed to retrieve activity info for widget configuration: $provider", e)
        null
    }

    override fun buildConfigureIntent(widgetId: Int, provider: ComponentName) =
        Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE).apply {
            component = provider
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }

    override fun buildBindIntent(widgetId: Int, provider: ComponentName) =
        Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, provider)
        }

    override fun deleteWidgetId(widgetId: Int) {
        appWidgetHost.deleteAppWidgetId(widgetId)
    }

    private companion object {
        const val TAG = "WidgetManager"
    }
}
