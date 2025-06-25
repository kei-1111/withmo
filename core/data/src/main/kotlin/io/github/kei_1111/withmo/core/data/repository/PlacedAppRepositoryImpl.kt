package io.github.kei_1111.withmo.core.data.repository

import androidx.compose.ui.geometry.Offset
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.PlacedAppDao
import io.github.kei_1111.withmo.core.data.local.entity.PlacedAppEntity
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.domain.repository.PlacedAppRepository
import io.github.kei_1111.withmo.core.model.PlacedApp
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

    override val placedApps: Flow<List<PlacedApp>> = combine(
        placedAppDao.getAll(),
        appManager.appInfoList,
    ) { placedAppEntities, appInfoList ->
        placedAppEntities.mapNotNull { entity ->
            val appInfo = appInfoList.find { it.packageName == entity.packageName }
            appInfo?.let {
                PlacedApp(
                    id = entity.id,
                    info = it,
                    position = Offset(entity.positionX, entity.positionY),
                )
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun updatePlacedApps(placedApps: List<PlacedApp>) {
        withContext(ioDispatcher) {
            val entities = placedApps.map { placedApp ->
                PlacedAppEntity(
                    id = placedApp.id,
                    packageName = placedApp.info.packageName,
                    positionX = placedApp.position.x,
                    positionY = placedApp.position.y,
                )
            }
            placedAppDao.deleteAll()
            placedAppDao.insertAll(entities)
        }
    }
}
