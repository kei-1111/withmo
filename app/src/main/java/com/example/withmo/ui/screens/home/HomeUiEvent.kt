package com.example.withmo.ui.screens.home

sealed interface HomeUiEvent {
    data class SetShowScaleSlider(val isShow: Boolean) : HomeUiEvent
    data class SetPopupExpanded(val isExpand: Boolean) : HomeUiEvent
    data class OnValueChangeAppSearchQuery(val query: String) : HomeUiEvent
    data object OnSelectSortByUsageOrder : HomeUiEvent
    data object OnSelectSortByAlphabeticalOrder : HomeUiEvent
}
