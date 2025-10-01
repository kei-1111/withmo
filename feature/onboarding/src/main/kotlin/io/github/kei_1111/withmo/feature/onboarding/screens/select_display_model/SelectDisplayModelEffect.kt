package io.github.kei_1111.withmo.feature.onboarding.screens.select_display_model

import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface SelectDisplayModelEffect : Effect {
    data object OpenDocument : SelectDisplayModelEffect
    data object NavigateBack : SelectDisplayModelEffect
    data object NavigateFinish : SelectDisplayModelEffect
    data class ShowToast(val message: String) : SelectDisplayModelEffect
}
