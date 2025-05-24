package io.github.kei_1111.withmo.ui.screens.onboarding

import io.github.kei_1111.withmo.ui.base.Effect

sealed interface OnboardingEffect : Effect {
    data object OpenDocument : OnboardingEffect
    data object NavigateHome : OnboardingEffect
    data class ShowToast(val message: String) : OnboardingEffect
}
