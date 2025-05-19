package io.github.kei_1111.withmo.ui.screens.home

import android.appwidget.AppWidgetProviderInfo
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data class OnAppClick(val appInfo: AppInfo) : HomeUiEvent
    data class OnAppLongClick(val appInfo: AppInfo) : HomeUiEvent
    data object OnShowScaleSliderButtonClick : HomeUiEvent
    data object OnCloseScaleSliderButtonClick : HomeUiEvent
    data object OnSetDefaultModelButtonClick : HomeUiEvent
    data object OnOpenDocumentButtonClick : HomeUiEvent
    data object OnNavigateSettingsButtonClick : HomeUiEvent
    data object OnModelChangeWarningDialogConfirm : HomeUiEvent
    data object OnModelChangeWarningDialogDismiss : HomeUiEvent
    data class OnAppSearchQueryChange(val query: String) : HomeUiEvent
    data object OnAppListSheetSwipeUp : HomeUiEvent
    data object OnAppListSheetSwipeDown : HomeUiEvent
    data object OnAddWidgetButtonClick : HomeUiEvent
    data object OnWidgetListSheetSwipeDown : HomeUiEvent
    data object OnDisplayModelContentSwipeLeft : HomeUiEvent
    data object OnWidgetContentSwipeRight : HomeUiEvent
    data class OnDisplayModelContentClick(val x: Float, val y: Float) : HomeUiEvent
    data object OnDisplayModelContentLongClick : HomeUiEvent
    data class OnWidgetListSheetItemClick(val widgetInfo: AppWidgetProviderInfo) : HomeUiEvent
    data object OnWidgetContentLongClick : HomeUiEvent
    data object OnCompleteEditButtonClick : HomeUiEvent
    data class OnDeleteWidgetBadgeClick(val withmoWidgetInfo: WithmoWidgetInfo) : HomeUiEvent
    data class OnResizeWidgetBadgeClick(val withmoWidgetInfo: WithmoWidgetInfo) : HomeUiEvent
    data class OnWidgetResizeBottomSheetClose(val withmoWidgetInfo: WithmoWidgetInfo) : HomeUiEvent
}
