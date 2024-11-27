package com.example.withmo.domain.repository

import com.example.withmo.domain.model.WidgetInfo
import kotlinx.coroutines.flow.Flow

interface WidgetInfoRepository {
    fun getAllWidgetList(): Flow<List<WidgetInfo>>

    suspend fun insertWidget(widgetInfoList: List<WidgetInfo>)

    suspend fun updateWidget(widgetInfoList: List<WidgetInfo>)

    suspend fun deleteWidget(widgetInfoList: List<WidgetInfo>)
}
