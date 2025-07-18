package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.PlacedAppRepository
import io.github.kei_1111.withmo.core.domain.repository.PlacedWidgetRepository
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import javax.inject.Inject

interface SavePlacedItemsUseCase {
    suspend operator fun invoke(placedItemList: List<PlaceableItem>)
}

class SavePlacedItemsUseCaseImpl @Inject constructor(
    private val placedAppRepository: PlacedAppRepository,
    private val placedWidgetRepository: PlacedWidgetRepository,
) : SavePlacedItemsUseCase {
    override suspend operator fun invoke(placedItemList: List<PlaceableItem>) {
        // Widget処理
        val placedWidgetInfoList = placedItemList.filterIsInstance<PlacedWidgetInfo>()
        placedWidgetRepository.updatePlacedWidgets(placedWidgetInfoList)

        // App処理
        val placedAppInfoList = placedItemList.filterIsInstance<PlacedAppInfo>()
        placedAppRepository.updatePlacedApps(placedAppInfoList)
    }
}
