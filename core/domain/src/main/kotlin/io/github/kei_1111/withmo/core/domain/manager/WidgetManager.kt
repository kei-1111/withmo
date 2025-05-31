package io.github.kei_1111.withmo.core.domain.manager

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo

interface WidgetManager {
    fun allocateId(): Int

    fun bindAppWidgetId(
        widgetId: Int,
        provider: ComponentName,
        minWidth: Int,
        minHeight: Int,
    ): Boolean

    fun getActivityInfo(provider: ComponentName): ActivityInfo?

    fun buildBindIntent(widgetId: Int, provider: ComponentName): Intent

    fun buildConfigureIntent(widgetId: Int, provider: ComponentName): Intent

    fun deleteWidgetId(widgetId: Int)
}
