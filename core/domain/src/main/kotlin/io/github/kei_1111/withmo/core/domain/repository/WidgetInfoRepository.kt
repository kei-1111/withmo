package io.github.kei_1111.withmo.core.domain.repository

import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo
import kotlinx.coroutines.flow.Flow

interface WidgetInfoRepository {
    fun getAllList(): Flow<List<WithmoWidgetInfo>>

    suspend fun insert(withmoWidgetInfoList: List<WithmoWidgetInfo>)

    suspend fun update(withmoWidgetInfoList: List<WithmoWidgetInfo>)

    suspend fun delete(withmoWidgetInfoList: List<WithmoWidgetInfo>)
}
