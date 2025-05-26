package io.github.kei_1111.withmo.ui.screens.sort_settings

import io.github.kei_1111.withmo.domain.model.user_settings.SortType
import io.github.kei_1111.withmo.ui.base.Action

sealed interface SortSettingsAction : Action {
    data class OnSortTypeRadioButtonClick(val sortType: SortType) : SortSettingsAction
    data object OnSaveButtonClick : SortSettingsAction
    data object OnBackButtonClick : SortSettingsAction
}
