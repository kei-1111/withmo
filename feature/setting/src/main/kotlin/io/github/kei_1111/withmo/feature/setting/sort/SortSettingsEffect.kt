package io.github.kei_1111.withmo.feature.setting.sort

import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface SortSettingsEffect : Effect {
    data object NavigateBack : SortSettingsEffect
    data class ShowToast(val message: String) : SortSettingsEffect
}
