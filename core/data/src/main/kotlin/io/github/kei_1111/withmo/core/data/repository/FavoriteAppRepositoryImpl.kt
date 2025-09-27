package io.github.kei_1111.withmo.core.data.repository

import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.FavoriteAppDao
import io.github.kei_1111.withmo.core.data.local.mapper.toEntity
import io.github.kei_1111.withmo.core.data.local.mapper.toFavoriteAppInfo
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteAppRepositoryImpl @Inject constructor(
    private val favoriteAppDao: FavoriteAppDao,
    private val appManager: AppManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FavoriteAppRepository {

    override val favoriteAppsInfo: Flow<Result<List<FavoriteAppInfo>>> = combine(
        favoriteAppDao.getAll(),
        appManager.appInfoList,
    ) { favoriteAppEntities, appInfoList ->
        runCatching {
            favoriteAppEntities.mapNotNull { entity ->
                val appInfo = appInfoList.find { it.packageName == entity.packageName }
                appInfo?.let { entity.toFavoriteAppInfo(it) }
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun updateFavoriteApps(favoriteAppInfos: List<FavoriteAppInfo>) {
        withContext(ioDispatcher) {
            val entities = favoriteAppInfos.map { it.toEntity() }
            favoriteAppDao.deleteAll()
            favoriteAppDao.insertAll(entities)
        }
    }
}
