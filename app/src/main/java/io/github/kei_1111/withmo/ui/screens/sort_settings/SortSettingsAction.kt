package io.github.kei_1111.withmo.ui.screens.sort_settings

import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.user_settings.SortType

sealed interface SortSettingsAction : Action {
    data class OnSortTypeRadioButtonClick(val sortType: SortType) : SortSettingsAction
    data object OnSaveButtonClick : SortSettingsAction
    data object OnBackButtonClick : SortSettingsAction
}
