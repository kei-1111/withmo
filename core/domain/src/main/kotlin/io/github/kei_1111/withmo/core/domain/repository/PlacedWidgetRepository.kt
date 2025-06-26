package io.github.kei_1111.withmo.core.domain.repository

import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import kotlinx.coroutines.flow.Flow

interface PlacedWidgetRepository {
    fun getAllList(): Flow<List<PlacedWidgetInfo>>

    suspend fun insert(placedWidgetInfoList: List<PlacedWidgetInfo>)

    suspend fun update(placedWidgetInfoList: List<PlacedWidgetInfo>)

    suspend fun delete(placedWidgetInfoList: List<PlacedWidgetInfo>)
}
