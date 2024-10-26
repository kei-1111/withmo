package com.example.withmo.ui.screens.onboarding

import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.base.UiEvent

sealed interface OnboardingUiEvent : UiEvent {
    data class OnValueChangeAppSearchQuery(val query: String) : OnboardingUiEvent
    data class AddSelectedAppList(val appInfo: AppInfo) : OnboardingUiEvent
    data class RemoveSelectedAppList(val appInfo: AppInfo) : OnboardingUiEvent
    data object RequestExternalStoragePermission : OnboardingUiEvent
    data class SelectModelFile(val modelFile: ModelFile) : OnboardingUiEvent
    data object NavigateToNextPage : OnboardingUiEvent
    data object NavigateToPreviousPage : OnboardingUiEvent
    data object OnboardingFinished : OnboardingUiEvent
}
