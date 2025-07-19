package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.PlacedAppRepository
import io.github.kei_1111.withmo.core.domain.repository.PlacedWidgetRepository
import io.github.kei_1111.withmo.core.model.PlaceableItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface GetPlacedItemsUseCase {
    operator fun invoke(): Flow<List<PlaceableItem>>
}

class GetPlacedItemsUseCaseImpl @Inject constructor(
    private val placedAppRepository: PlacedAppRepository,
    private val placedWidgetRepository: PlacedWidgetRepository,
) : GetPlacedItemsUseCase {
    override operator fun invoke(): Flow<List<PlaceableItem>> =
        combine(
            placedWidgetRepository.placedWidgetsInfo,
            placedAppRepository.placedAppsInfo,
        ) { widgetList, placedAppList ->
            (widgetList + placedAppList)
        }
}
