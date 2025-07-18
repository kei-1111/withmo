package io.github.kei_1111.withmo.core.data.repository

import android.appwidget.AppWidgetManager
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.PlacedWidgetDao
import io.github.kei_1111.withmo.core.data.local.mapper.toEntity
import io.github.kei_1111.withmo.core.data.local.mapper.toPlacedWidgetInfo
import io.github.kei_1111.withmo.core.domain.repository.PlacedWidgetRepository
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacedWidgetRepositoryImpl @Inject constructor(
    private val placedWidgetDao: PlacedWidgetDao,
    private val appWidgetManager: AppWidgetManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PlacedWidgetRepository {

    override fun getAllList(): Flow<List<PlacedWidgetInfo>> {
        return placedWidgetDao.getAll()
            .map { entities ->
                entities.mapNotNull { entity -> entity.toPlacedWidgetInfo(appWidgetManager) }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun updatePlacedWidgets(placedWidgetInfoList: List<PlacedWidgetInfo>) {
        withContext(ioDispatcher) {
            val entities = placedWidgetInfoList.map { it.toEntity() }
            placedWidgetDao.deleteAll()
            placedWidgetDao.insertAll(entities)
        }
    }

    private companion object {
        const val TAG = "PlacedWidgetRepository"
    }
}
