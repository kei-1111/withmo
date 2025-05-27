package io.github.kei_1111.withmo.ui.screens.sort_settings

import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface SortSettingsEffect : Effect {
    data object NavigateBack : SortSettingsEffect
    data class ShowToast(val message: String) : SortSettingsEffect
}
