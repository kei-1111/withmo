package com.example.withmo.ui.screens.home

import com.example.withmo.ui.base.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data class SetShowScaleSlider(val isShow: Boolean) : HomeUiEvent
    data class SetPopupExpanded(val isExpand: Boolean) : HomeUiEvent
    data class OnValueChangeAppSearchQuery(val query: String) : HomeUiEvent
    data object OnSelectSortByUsageOrder : HomeUiEvent
    data object OnSelectSortByAlphabeticalOrder : HomeUiEvent
}
