package io.github.kei_1111.withmo.core.data.repository

import android.appwidget.AppWidgetManager
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.WithmoWidgetInfoDao
import io.github.kei_1111.withmo.core.data.local.mapper.toEntity
import io.github.kei_1111.withmo.core.data.local.mapper.toWidgetInfo
import io.github.kei_1111.withmo.core.domain.repository.WidgetInfoRepository
import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WidgetInfoRepositoryImpl @Inject constructor(
    private val withmoWidgetInfoDao: WithmoWidgetInfoDao,
    private val appWidgetManager: AppWidgetManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WidgetInfoRepository {

    override fun getAllList(): Flow<List<WithmoWidgetInfo>> {
        return withmoWidgetInfoDao.getAllList()
            .map { entities ->
                entities.mapNotNull { entity -> entity.toWidgetInfo(appWidgetManager) }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun insert(withmoWidgetInfoList: List<WithmoWidgetInfo>) {
        withContext(ioDispatcher) {
            withmoWidgetInfoDao.insert(withmoWidgetInfoList.map { it.toEntity() })
        }
    }

    override suspend fun update(withmoWidgetInfoList: List<WithmoWidgetInfo>) {
        withContext(ioDispatcher) {
            withmoWidgetInfoDao.update(withmoWidgetInfoList.map { it.toEntity() })
        }
    }

    override suspend fun delete(withmoWidgetInfoList: List<WithmoWidgetInfo>) {
        withContext(ioDispatcher) {
            withmoWidgetInfoDao.delete(withmoWidgetInfoList.map { it.toEntity() })
        }
    }

    private companion object {
        const val TAG = "WidgetInfoRepository"
    }
}
