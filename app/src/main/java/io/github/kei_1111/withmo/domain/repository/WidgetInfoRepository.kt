package io.github.kei_1111.withmo.domain.repository

import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo
import kotlinx.coroutines.flow.Flow

interface WidgetInfoRepository {
    fun getAllWidgetList(): Flow<List<WithmoWidgetInfo>>

    suspend fun insertWidget(withmoWidgetInfoList: List<WithmoWidgetInfo>)

    suspend fun updateWidget(withmoWidgetInfoList: List<WithmoWidgetInfo>)

    suspend fun deleteWidget(withmoWidgetInfoList: List<WithmoWidgetInfo>)
}
