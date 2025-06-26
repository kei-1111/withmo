package io.github.kei_1111.withmo.core.domain.repository

import io.github.kei_1111.withmo.core.model.PlacedWidget
import kotlinx.coroutines.flow.Flow

interface PlacedWidgetRepository {
    fun getAllList(): Flow<List<PlacedWidget>>

    suspend fun insert(placedWidgetList: List<PlacedWidget>)

    suspend fun update(placedWidgetList: List<PlacedWidget>)

    suspend fun delete(placedWidgetList: List<PlacedWidget>)
}
