package com.example.withmo.ui.screens.sort_settings

import com.example.withmo.domain.model.user_settings.SortType
import com.example.withmo.ui.base.UiEvent

sealed interface SortSettingsUiEvent : UiEvent {
    data class ChangeSortType(val sortType: SortType) : SortSettingsUiEvent
    data object Save : SortSettingsUiEvent
    data object SaveSuccess : SortSettingsUiEvent
    data object SaveFailure : SortSettingsUiEvent
    data object NavigateToSettingsScreen : SortSettingsUiEvent
}
