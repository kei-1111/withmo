package io.github.kei_1111.withmo.core.domain.repository

import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import kotlinx.coroutines.flow.Flow

interface AppInfoRepository {
    fun getAllList(): Flow<List<WithmoAppInfo>>

    fun getFavoriteList(): Flow<List<WithmoAppInfo>>

    suspend fun getByPackageName(packageName: String): WithmoAppInfo?

    suspend fun insert(withmoAppInfo: WithmoAppInfo)

    suspend fun update(withmoAppInfo: WithmoAppInfo)

    suspend fun updateList(withmoAppInfoList: List<WithmoAppInfo>)

    suspend fun delete(withmoAppInfo: WithmoAppInfo)

    suspend fun syncWithInstalledApps(installedApps: List<WithmoAppInfo>)
}
