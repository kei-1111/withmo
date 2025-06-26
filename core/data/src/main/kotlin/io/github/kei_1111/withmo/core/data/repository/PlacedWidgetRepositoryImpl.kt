package io.github.kei_1111.withmo.core.data.repository

import android.appwidget.AppWidgetManager
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.PlacedWidgetDao
import io.github.kei_1111.withmo.core.data.local.mapper.toEntity
import io.github.kei_1111.withmo.core.data.local.mapper.toWidgetInfo
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
        return placedWidgetDao.getAllList()
            .map { entities ->
                entities.mapNotNull { entity -> entity.toWidgetInfo(appWidgetManager) }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun insert(placedWidgetInfoList: List<PlacedWidgetInfo>) {
        withContext(ioDispatcher) {
            placedWidgetDao.insert(placedWidgetInfoList.map { it.toEntity() })
        }
    }

    override suspend fun update(placedWidgetInfoList: List<PlacedWidgetInfo>) {
        withContext(ioDispatcher) {
            placedWidgetDao.update(placedWidgetInfoList.map { it.toEntity() })
        }
    }

    override suspend fun delete(placedWidgetInfoList: List<PlacedWidgetInfo>) {
        withContext(ioDispatcher) {
            placedWidgetDao.delete(placedWidgetInfoList.map { it.toEntity() })
        }
    }

    private companion object {
        const val TAG = "PlacedWidgetRepository"
    }
}
