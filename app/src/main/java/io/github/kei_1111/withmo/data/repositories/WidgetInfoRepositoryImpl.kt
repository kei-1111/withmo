package io.github.kei_1111.withmo.data.repositories

import android.appwidget.AppWidgetManager
import io.github.kei_1111.withmo.data.local.dao.WidgetInfoDao
import io.github.kei_1111.withmo.data.mapper.toEntity
import io.github.kei_1111.withmo.data.mapper.toWidgetInfo
import io.github.kei_1111.withmo.di.IoDispatcher
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.repository.WidgetInfoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WidgetInfoRepositoryImpl @Inject constructor(
    private val widgetInfoDao: WidgetInfoDao,
    private val appWidgetManager: AppWidgetManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WidgetInfoRepository {

    override fun getAllWidgetList(): Flow<List<WidgetInfo>> {
        return widgetInfoDao.getAllWidgets().map { entities ->
            entities.mapNotNull { entity -> entity.toWidgetInfo(appWidgetManager) }
        }
    }

    override suspend fun insertWidget(widgetInfoList: List<WidgetInfo>) {
        withContext(ioDispatcher) {
            widgetInfoDao.insert(widgetInfoList.map { it.toEntity() })
        }
    }

    override suspend fun updateWidget(widgetInfoList: List<WidgetInfo>) {
        withContext(ioDispatcher) {
            widgetInfoDao.update(widgetInfoList.map { it.toEntity() })
        }
    }

    override suspend fun deleteWidget(widgetInfoList: List<WidgetInfo>) {
        withContext(ioDispatcher) {
            widgetInfoDao.delete(widgetInfoList.map { it.toEntity() })
        }
    }
}
