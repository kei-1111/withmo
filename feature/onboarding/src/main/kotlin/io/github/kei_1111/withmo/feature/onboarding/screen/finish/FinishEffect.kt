package io.github.kei_1111.withmo.feature.onboarding.screen.finish

import io.github.kei_1111.withmo.core.featurebase.Effect

internal sealed interface FinishEffect : Effect {
    data object NavigateBack : FinishEffect
    data object NavigateHome : FinishEffect
}
