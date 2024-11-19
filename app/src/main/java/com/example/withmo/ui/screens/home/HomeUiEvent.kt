package com.example.withmo.ui.screens.home

import android.appwidget.AppWidgetProviderInfo
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.WidgetInfo
import com.example.withmo.ui.base.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data class StartApp(val appInfo: AppInfo) : HomeUiEvent
    data class DeleteApp(val appInfo: AppInfo) : HomeUiEvent
    data class SetShowScaleSlider(val isShow: Boolean) : HomeUiEvent
    data class OnValueChangeAppSearchQuery(val query: String) : HomeUiEvent
    data object OpenAppListBottomSheet : HomeUiEvent
    data object HideAppListBottomSheet : HomeUiEvent
    data object OpenWidgetListBottomSheet : HomeUiEvent
    data object HideWidgetListBottomSheet : HomeUiEvent
    data class OnSelectWidget(val widgetInfo: AppWidgetProviderInfo) : HomeUiEvent
    data object EnterEditMode : HomeUiEvent
    data object ExitEditMode : HomeUiEvent
    data class DeleteWidget(val widgetInfo: WidgetInfo) : HomeUiEvent
    data object NavigateToSettingsScreen : HomeUiEvent
}
