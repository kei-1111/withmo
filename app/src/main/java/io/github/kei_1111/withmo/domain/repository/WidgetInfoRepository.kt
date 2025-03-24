package io.github.kei_1111.withmo.domain.repository

import io.github.kei_1111.withmo.domain.model.WidgetInfo
import kotlinx.coroutines.flow.Flow

interface WidgetInfoRepository {
    fun getAllWidgetList(): Flow<List<WidgetInfo?>>

    suspend fun insertWidget(widgetInfoList: List<WidgetInfo>)

    suspend fun updateWidget(widgetInfoList: List<WidgetInfo>)

    suspend fun deleteWidget(widgetInfoList: List<WidgetInfo>)
}
