package com.example.withmo.data.repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.withmo.data.local.dao.AppInfoDao
import com.example.withmo.data.mapper.toAppInfo
import com.example.withmo.data.mapper.toEntity
import com.example.withmo.di.IoDispatcher
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.repository.AppInfoRepository
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
