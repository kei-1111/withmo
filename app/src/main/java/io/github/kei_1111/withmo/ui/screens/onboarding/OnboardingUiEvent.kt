package io.github.kei_1111.withmo.ui.screens.onboarding

import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface OnboardingUiEvent : UiEvent {
    data class OnValueChangeAppSearchQuery(val query: String) : OnboardingUiEvent
    data class AddSelectedAppList(val appInfo: AppInfo) : OnboardingUiEvent
    data class RemoveSelectedAppList(val appInfo: AppInfo) : OnboardingUiEvent
    data object OnOpenDocumentButtonClick : OnboardingUiEvent
    data object NavigateToNextPage : OnboardingUiEvent
    data object NavigateToPreviousPage : OnboardingUiEvent
    data object OnboardingFinished : OnboardingUiEvent
}
