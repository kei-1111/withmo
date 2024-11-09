package com.example.withmo.domain.repository

import com.example.withmo.domain.model.AppInfo
import kotlinx.coroutines.flow.Flow

interface AppInfoRepository {
    fun getAllAppInfoList(): Flow<List<AppInfo>>

    fun getFavoriteAppInfoList(): Flow<List<AppInfo>>

    suspend fun getAppInfoByPackageName(packageName: String): AppInfo?

    suspend fun insertAppInfo(appInfo: AppInfo)

    suspend fun updateAppInfo(appInfo: AppInfo)

    suspend fun updateAppInfoList(appInfoList: List<AppInfo>)

    suspend fun deleteAppInfo(appInfo: AppInfo)

    suspend fun syncWithInstalledApps(installedApps: List<AppInfo>)
}
