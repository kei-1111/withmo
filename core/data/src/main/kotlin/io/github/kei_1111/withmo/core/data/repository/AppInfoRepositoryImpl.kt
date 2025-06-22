package io.github.kei_1111.withmo.core.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.WithmoAppInfoDao
import io.github.kei_1111.withmo.core.data.local.mapper.toAppInfo
import io.github.kei_1111.withmo.core.data.local.mapper.toEntity
import io.github.kei_1111.withmo.core.domain.manager.AppUsageStatsManager
import io.github.kei_1111.withmo.core.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppInfoRepositoryImpl @Inject constructor(
    private val withmoAppInfoDao: WithmoAppInfoDao,
    private val context: Context,
    private val appUsageStatsManager: AppUsageStatsManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AppInfoRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllList(): Flow<List<WithmoAppInfo>> {
        return withmoAppInfoDao.getAllList()
            .map { entities ->
                entities.mapNotNull { entity -> entity.toAppInfo(context) }
            }
            .flowOn(ioDispatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFavoriteList(): Flow<List<WithmoAppInfo>> {
        return withmoAppInfoDao.getFavoriteList()
            .map { entities ->
                entities
                    .mapNotNull { entity -> entity.toAppInfo(context) }
                    .sortedBy { it.favoriteOrder.ordinal }
            }
            .flowOn(ioDispatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPlacedList(): Flow<List<WithmoAppInfo>> {
        return withmoAppInfoDao.getPlacedList()
            .map { entities ->
                entities.mapNotNull { entity -> entity.toAppInfo(context) }
            }
            .flowOn(ioDispatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getByPackageName(packageName: String): WithmoAppInfo? {
        return withContext(ioDispatcher) {
            withmoAppInfoDao.getByPackageName(packageName)?.toAppInfo(context)
                ?: return@withContext null
        }
    }

    override suspend fun insert(withmoAppInfo: WithmoAppInfo) {
        withContext(ioDispatcher) {
            withmoAppInfoDao.insert(withmoAppInfo.toEntity())
        }
    }

    override suspend fun update(withmoAppInfo: WithmoAppInfo) {
        withContext(ioDispatcher) {
            withmoAppInfoDao.update(withmoAppInfo.toEntity())
        }
    }

    override suspend fun updateList(withmoAppInfoList: List<WithmoAppInfo>) {
        withContext(ioDispatcher) {
            withmoAppInfoDao.updateList(withmoAppInfoList.map { it.toEntity() })
        }
    }

    override suspend fun delete(withmoAppInfo: WithmoAppInfo) {
        withContext(ioDispatcher) {
            withmoAppInfoDao.delete(withmoAppInfo.toEntity())
        }
    }

    override suspend fun syncWithInstalledApps(installedApps: List<WithmoAppInfo>) {
        withContext(ioDispatcher) {
            val roomAppEntities = withmoAppInfoDao.getAllList().first()

            val installedAppPackages = installedApps.map { it.info.packageName }.toSet()
            val roomAppPackages = roomAppEntities.map { it.packageName }.toSet()

            val newApps = installedApps.filter { it.info.packageName !in roomAppPackages }
            newApps.forEach { insert(it) }

            val removedEntities = roomAppEntities.filter { it.packageName !in installedAppPackages }
            removedEntities.forEach { withmoAppInfoDao.delete(it) }
        }
    }

    override suspend fun updateUsageCounts() {
        withContext(ioDispatcher) {
            val usageCounts = appUsageStatsManager.getAppUsageCounts()

            if (usageCounts.isNotEmpty()) {
                val allApps = withmoAppInfoDao.getAllList().first()
                val updatedApps = allApps.mapNotNull { entity ->
                    val usageCount = usageCounts[entity.packageName] ?: 0
                    if (entity.useCount != usageCount) {
                        entity.copy(useCount = usageCount)
                    } else {
                        null
                    }
                }

                if (updatedApps.isNotEmpty()) {
                    withmoAppInfoDao.updateList(updatedApps)
                }
            }
        }
    }

    private companion object {
        const val TAG = "AppInfoRepository"
    }
}
