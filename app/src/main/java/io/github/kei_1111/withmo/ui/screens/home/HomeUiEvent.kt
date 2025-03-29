package io.github.kei_1111.withmo.ui.screens.home

import android.appwidget.AppWidgetProviderInfo
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data class StartApp(val appInfo: AppInfo) : HomeUiEvent
    data class DeleteApp(val appInfo: AppInfo) : HomeUiEvent
    data class SetShowScaleSlider(val isShow: Boolean) : HomeUiEvent
    data object OnOpenDocumentButtonClick : HomeUiEvent
    data class OnValueChangeAppSearchQuery(val query: String) : HomeUiEvent
    data object OpenAppListBottomSheet : HomeUiEvent
    data object HideAppListBottomSheet : HomeUiEvent
    data object OpenWidgetListBottomSheet : HomeUiEvent
    data object HideWidgetListBottomSheet : HomeUiEvent
    data class OnSelectWidget(val widgetInfo: AppWidgetProviderInfo) : HomeUiEvent
    data object EnterEditMode : HomeUiEvent
    data object ExitEditMode : HomeUiEvent
    data class DeleteWidget(val widgetInfo: WidgetInfo) : HomeUiEvent
    data class ResizeWidget(val widgetInfo: WidgetInfo) : HomeUiEvent
    data class FinishResizeWidget(val widgetInfo: WidgetInfo) : HomeUiEvent
    data object NavigateToSettingsScreen : HomeUiEvent
}
