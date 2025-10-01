package io.github.kei_1111.withmo.feature.onboarding.screens.finish

import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface FinishEffect : Effect {
    data object NavigateBack : FinishEffect
    data object NavigateHome : FinishEffect
}
