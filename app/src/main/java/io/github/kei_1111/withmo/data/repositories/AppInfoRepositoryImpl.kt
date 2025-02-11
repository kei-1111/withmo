package io.github.kei_1111.withmo.data.repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.data.local.dao.AppInfoDao
import io.github.kei_1111.withmo.data.mapper.toAppInfo
import io.github.kei_1111.withmo.data.mapper.toEntity
import io.github.kei_1111.withmo.di.IoDispatcher
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
        return appInfoDao.getAllAppInfoList().map { entities ->
            entities.map { entity -> entity.toAppInfo(context) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFavoriteAppInfoList(): Flow<List<AppInfo>> {
        return appInfoDao.getFavoriteAppInfoList().map { entities ->
            entities
                .map { entity -> entity.toAppInfo(context) }
                .sortedBy { it.favoriteOrder.ordinal }
        }
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
}
