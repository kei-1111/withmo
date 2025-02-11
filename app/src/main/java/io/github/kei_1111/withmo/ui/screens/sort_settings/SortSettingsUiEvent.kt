package io.github.kei_1111.withmo.ui.screens.sort_settings

import io.github.kei_1111.withmo.domain.model.user_settings.SortType
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface SortSettingsUiEvent : UiEvent {
    data class ChangeSortType(val sortType: SortType) : SortSettingsUiEvent
    data object Save : SortSettingsUiEvent
    data object SaveSuccess : SortSettingsUiEvent
    data object SaveFailure : SortSettingsUiEvent
    data object NavigateToSettingsScreen : SortSettingsUiEvent
}
