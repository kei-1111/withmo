package io.github.kei_1111.withmo.ui.screens.onboarding

import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface OnboardingUiEvent : UiEvent {
    data class OnAppSearchQueryChange(val query: String) : OnboardingUiEvent
    data class OnAllAppListAppClick(val appInfo: AppInfo) : OnboardingUiEvent
    data class OnFavoriteAppListAppClick(val appInfo: AppInfo) : OnboardingUiEvent
    data object OnOpenDocumentButtonClick : OnboardingUiEvent
    data object OnNextButtonClick : OnboardingUiEvent
    data object OnPreviousButtonClick : OnboardingUiEvent
}
