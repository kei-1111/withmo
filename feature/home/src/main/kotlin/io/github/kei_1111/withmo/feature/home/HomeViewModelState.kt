package io.github.kei_1111.withmo.feature.home

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeViewModelState(
    val isChangeModelScaleContentShown: Boolean = false,
    val isModelChangeWarningDialogShown: Boolean = false,
    val isModelLoading: Boolean = false,
    val isAppListSheetOpened: Boolean = false,
    val isPlaceableItemListSheetOpened: Boolean = false,
    val placedItemList: ImmutableList<PlaceableItem> = persistentListOf(),
    val pendingWidgetInfo: WidgetInfo? = null,
    val isEditMode: Boolean = false,
    val resizingWidget: PlacedWidgetInfo? = null,
    val isWidgetResizing: Boolean = false,
    val favoriteAppInfoList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val currentPage: PageContent = PageContent.DisplayModel,
    val currentUserSettings: UserSettings = UserSettings(),
) : ViewModelState<HomeState> {
    override fun toState() = HomeState(
        isChangeModelScaleContentShown = isChangeModelScaleContentShown,
        isModelChangeWarningDialogShown = isModelChangeWarningDialogShown,
        isModelLoading = isModelLoading,
        isAppListSheetOpened = isAppListSheetOpened,
        isPlaceableItemListSheetOpened = isPlaceableItemListSheetOpened,
        placedItemList = placedItemList,
        isEditMode = isEditMode,
        resizingWidget = resizingWidget,
        isWidgetResizing = isWidgetResizing,
        favoriteAppInfoList = favoriteAppInfoList,
        currentPage = currentPage,
        currentUserSettings = currentUserSettings,
    )
}
