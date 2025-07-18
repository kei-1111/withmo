package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.PlacedAppRepository
import io.github.kei_1111.withmo.core.domain.repository.PlacedWidgetRepository
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import javax.inject.Inject

interface SavePlacedItemsUseCase {
    suspend operator fun invoke(
        currentPlacedItemList: List<PlaceableItem>,
        initialPlacedItemList: List<PlaceableItem>,
    )
}

class SavePlacedItemsUseCaseImpl @Inject constructor(
    private val placedAppRepository: PlacedAppRepository,
    private val placedWidgetRepository: PlacedWidgetRepository,
) : SavePlacedItemsUseCase {
    override suspend operator fun invoke(
        currentPlacedItemList: List<PlaceableItem>,
        initialPlacedItemList: List<PlaceableItem>,
    ) {
        val addedPlacedItemList = currentPlacedItemList.filterNot { currentPlaceableItem ->
            initialPlacedItemList.any { initialPlaceableItem -> initialPlaceableItem.id == currentPlaceableItem.id }
        }

        val updatedPlacedItemList = currentPlacedItemList.filter { currentPlaceableItem ->
            initialPlacedItemList.any { initialPlaceableItem -> initialPlaceableItem.id == currentPlaceableItem.id }
        }

        val deletedPlacedItemList = initialPlacedItemList.filterNot { initialPlaceableItem ->
            currentPlacedItemList.any { currentPlaceableItem -> initialPlaceableItem.id == currentPlaceableItem.id }
        }

        // Widget処理
        val addedWidgetList = addedPlacedItemList.filterIsInstance<PlacedWidgetInfo>()
        val updatedWidgetList = updatedPlacedItemList.filterIsInstance<PlacedWidgetInfo>()
        val deletedWidgetList = deletedPlacedItemList.filterIsInstance<PlacedWidgetInfo>()
        placedWidgetRepository.insert(addedWidgetList)
        placedWidgetRepository.update(updatedWidgetList)
        placedWidgetRepository.delete(deletedWidgetList)

        // App処理
        val placedAppInfoList = currentPlacedItemList.filterIsInstance<PlacedAppInfo>()
        placedAppRepository.updatePlacedApps(placedAppInfoList)
    }
}
