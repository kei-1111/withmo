package io.github.kei_1111.withmo.data.repositories

import android.appwidget.AppWidgetManager
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.data.local.dao.WidgetInfoDao
import io.github.kei_1111.withmo.data.mapper.toEntity
import io.github.kei_1111.withmo.data.mapper.toWidgetInfo
import io.github.kei_1111.withmo.domain.repository.WidgetInfoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WidgetInfoRepositoryImpl @Inject constructor(
    private val widgetInfoDao: WidgetInfoDao,
    private val appWidgetManager: AppWidgetManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WidgetInfoRepository {

    override fun getAllWidgetList(): Flow<List<WithmoWidgetInfo>> {
        return widgetInfoDao.getAllWidgets()
            .map { entities ->
                entities.mapNotNull { entity -> entity.toWidgetInfo(appWidgetManager) }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun insertWidget(withmoWidgetInfoList: List<WithmoWidgetInfo>) {
        withContext(ioDispatcher) {
            widgetInfoDao.insert(withmoWidgetInfoList.map { it.toEntity() })
        }
    }

    override suspend fun updateWidget(withmoWidgetInfoList: List<WithmoWidgetInfo>) {
        withContext(ioDispatcher) {
            widgetInfoDao.update(withmoWidgetInfoList.map { it.toEntity() })
        }
    }

    override suspend fun deleteWidget(withmoWidgetInfoList: List<WithmoWidgetInfo>) {
        withContext(ioDispatcher) {
            widgetInfoDao.delete(withmoWidgetInfoList.map { it.toEntity() })
        }
    }

    private companion object {
        const val TAG = "WidgetInfoRepository"
    }
}
