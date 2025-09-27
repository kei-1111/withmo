package io.github.kei_1111.withmo.core.data.repository

import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.PlacedAppDao
import io.github.kei_1111.withmo.core.data.local.mapper.toEntity
import io.github.kei_1111.withmo.core.data.local.mapper.toPlacedAppInfo
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.domain.repository.PlacedAppRepository
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacedAppRepositoryImpl @Inject constructor(
    private val placedAppDao: PlacedAppDao,
    private val appManager: AppManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PlacedAppRepository {

    override val placedAppsInfo: Flow<Result<List<PlacedAppInfo>>> = combine(
        placedAppDao.getAll(),
        appManager.appInfoList,
    ) { placedAppEntities, appInfoList ->
        runCatching {
            placedAppEntities.mapNotNull { entity ->
                val appInfo = appInfoList.find { it.packageName == entity.packageName }
                appInfo?.let { entity.toPlacedAppInfo(it) }
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun updatePlacedApps(placedAppInfos: List<PlacedAppInfo>) {
        withContext(ioDispatcher) {
            val entities = placedAppInfos.map { it.toEntity() }
            placedAppDao.deleteAll()
            placedAppDao.insertAll(entities)
        }
    }
}
