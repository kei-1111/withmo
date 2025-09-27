package io.github.kei_1111.withmo.core.domain.repository

import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import kotlinx.coroutines.flow.Flow

interface PlacedWidgetRepository {
    val placedWidgetsInfo: Flow<Result<List<PlacedWidgetInfo>>>

    suspend fun updatePlacedWidgets(placedWidgetInfoList: List<PlacedWidgetInfo>)
}
