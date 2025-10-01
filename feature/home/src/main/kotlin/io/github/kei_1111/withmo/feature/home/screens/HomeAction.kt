package io.github.kei_1111.withmo.feature.home.screens

import android.appwidget.AppWidgetProviderInfo
import android.net.Uri
import androidx.activity.result.ActivityResult
import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo

internal sealed interface HomeAction : Action {
    data class OnAppClick(val appInfo: AppInfo) : HomeAction
    data class OnAppLongClick(val appInfo: AppInfo) : HomeAction
    data object OnShowScaleSliderButtonClick : HomeAction
    data object OnCloseScaleSliderButtonClick : HomeAction
    data class OnScaleSliderChange(val scale: Float) : HomeAction
    data object OnSetDefaultModelButtonClick : HomeAction
    data object OnOpenDocumentButtonClick : HomeAction
    data object OnNavigateSettingsButtonClick : HomeAction
    data object OnModelChangeWarningDialogConfirm : HomeAction
    data object OnModelChangeWarningDialogDismiss : HomeAction
    data object OnAppListSheetSwipeUp : HomeAction
    data object OnAppListSheetSwipeDown : HomeAction
    data object OnAddPlaceableItemButtonClick : HomeAction
    data object OnPlaceableItemListSheetSwipeDown : HomeAction
    data object OnDisplayModelContentSwipeLeft : HomeAction
    data object OnPlaceableItemContentSwipeRight : HomeAction
    data class OnDisplayModelContentClick(val x: Float, val y: Float) : HomeAction
    data object OnDisplayModelContentLongClick : HomeAction
    data class OnPlaceableItemListSheetWidgetClick(val widgetInfo: AppWidgetProviderInfo) : HomeAction
    data class OnPlaceableItemListSheetAppClick(val appInfo: AppInfo) : HomeAction
    data object OnPlaceableItemContentLongClick : HomeAction
    data object OnCompleteEditButtonClick : HomeAction
    data class OnDeletePlaceableItemBadgeClick(val placeableItem: PlaceableItem) : HomeAction
    data class OnResizeWidgetBadgeClick(val placedWidgetInfo: PlacedWidgetInfo) : HomeAction
    data class OnWidgetResizeBottomSheetClose(val placedWidgetInfo: PlacedWidgetInfo) : HomeAction

    data class OnOpenDocumentLauncherResult(val uri: Uri?) : HomeAction
    data class OnConfigureWidgetLauncherResult(val result: ActivityResult) : HomeAction
    data class OnBindWidgetLauncherResult(val result: ActivityResult) : HomeAction
}
