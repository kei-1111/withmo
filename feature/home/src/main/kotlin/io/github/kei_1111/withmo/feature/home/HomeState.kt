package io.github.kei_1111.withmo.feature.home

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeState(
    val isChangeModelScaleContentShown: Boolean = false,
    val isModelChangeWarningDialogShown: Boolean = false,
    val isModelLoading: Boolean = false,
    val isAppListSheetOpened: Boolean = false,
    val isPlaceableItemListSheetOpened: Boolean = false,
    val placedItemList: ImmutableList<PlaceableItem> = persistentListOf(),
    val isEditMode: Boolean = false,
    val resizingWidget: PlacedWidgetInfo? = null,
    val isWidgetResizing: Boolean = false,
    val favoriteAppInfoList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val currentPage: PageContent = PageContent.DisplayModel,
    val currentUserSettings: UserSettings = UserSettings(),
) : State

enum class PageContent {
    DisplayModel,
    PlaceableItem,
}
