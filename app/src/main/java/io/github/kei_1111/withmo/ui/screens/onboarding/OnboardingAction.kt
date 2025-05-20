package io.github.kei_1111.withmo.ui.screens.onboarding

import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.base.Action

sealed interface OnboardingAction : Action {
    data class OnAppSearchQueryChange(val query: String) : OnboardingAction
    data class OnAllAppListAppClick(val appInfo: AppInfo) : OnboardingAction
    data class OnFavoriteAppListAppClick(val appInfo: AppInfo) : OnboardingAction
    data object OnSelectDisplayModelAreaClick : OnboardingAction
    data object OnNextButtonClick : OnboardingAction
    data object OnPreviousButtonClick : OnboardingAction
}
