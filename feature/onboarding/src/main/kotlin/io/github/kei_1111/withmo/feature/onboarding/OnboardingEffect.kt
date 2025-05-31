package io.github.kei_1111.withmo.feature.onboarding

import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface OnboardingEffect : Effect {
    data object OpenDocument : OnboardingEffect
    data object NavigateHome : OnboardingEffect
    data class ShowToast(val message: String) : OnboardingEffect
}
