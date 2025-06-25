package io.github.kei_1111.withmo.core.domain.manager

import io.github.kei_1111.withmo.core.model.AppInfo
import kotlinx.coroutines.flow.StateFlow

interface AppManager {
    val appInfoList: StateFlow<List<AppInfo>>

    suspend fun refreshAppList()

    fun updateNotifications(notificationMap: Map<String, Boolean>)
}
