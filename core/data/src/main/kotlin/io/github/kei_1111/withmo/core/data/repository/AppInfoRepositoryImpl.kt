package io.github.kei_1111.withmo.core.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.AppInfoDao
import io.github.kei_1111.withmo.core.data.local.mapper.toAppInfo
import io.github.kei_1111.withmo.core.data.local.mapper.toEntity
import io.github.kei_1111.withmo.core.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.core.model.AppInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppInfoRepositoryImpl @Inject constructor(
    private val appInfoDao: AppInfoDao,
    private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AppInfoRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllAppInfoList(): Flow<List<AppInfo>> {
        return appInfoDao.getAllAppInfoList()
            .map { entities ->
                entities.mapNotNull { entity -> entity.toAppInfo(context) }
            }
            .flowOn(ioDispatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFavoriteAppInfoList(): Flow<List<AppInfo>> {
        return appInfoDao.getFavoriteAppInfoList()
            .map { entities ->
                entities
                    .mapNotNull { entity -> entity.toAppInfo(context) }
                    .sortedBy { it.favoriteOrder.ordinal }
            }
            .flowOn(ioDispatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAppInfoByPackageName(packageName: String): AppInfo? {
        return withContext(ioDispatcher) {
            appInfoDao.getAppInfoByPackageName(packageName)?.toAppInfo(context)
                ?: return@withContext null
        }
    }

    override suspend fun insertAppInfo(appInfo: AppInfo) {
        withContext(ioDispatcher) {
            appInfoDao.insertAppInfo(appInfo.toEntity())
        }
    }

    override suspend fun updateAppInfo(appInfo: AppInfo) {
        withContext(ioDispatcher) {
            appInfoDao.updateAppInfo(appInfo.toEntity())
        }
    }

    override suspend fun updateAppInfoList(appInfoList: List<AppInfo>) {
        withContext(ioDispatcher) {
            appInfoDao.updateAppInfoList(appInfoList.map { it.toEntity() })
        }
    }

    override suspend fun deleteAppInfo(appInfo: AppInfo) {
        withContext(ioDispatcher) {
            appInfoDao.deleteAppInfo(appInfo.toEntity())
        }
    }

    override suspend fun syncWithInstalledApps(installedApps: List<AppInfo>) {
        withContext(ioDispatcher) {
            val roomAppEntities = appInfoDao.getAllAppInfoList().first()

            val installedAppPackages = installedApps.map { it.packageName }.toSet()
            val roomAppPackages = roomAppEntities.map { it.packageName }.toSet()

            val newApps = installedApps.filter { it.packageName !in roomAppPackages }
            newApps.forEach { insertAppInfo(it) }

            val removedEntities = roomAppEntities.filter { it.packageName !in installedAppPackages }
            removedEntities.forEach { appInfoDao.deleteAppInfo(it) }
        }
    }

    private companion object {
        const val TAG = "AppInfoRepository"
    }
}
