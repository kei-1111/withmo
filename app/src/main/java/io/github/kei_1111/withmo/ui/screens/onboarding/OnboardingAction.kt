package io.github.kei_1111.withmo.ui.screens.onboarding

import android.net.Uri
import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.domain.model.AppInfo

sealed interface OnboardingAction : Action {
    data class OnAppSearchQueryChange(val query: String) : OnboardingAction
    data class OnAllAppListAppClick(val appInfo: AppInfo) : OnboardingAction
    data class OnFavoriteAppListAppClick(val appInfo: AppInfo) : OnboardingAction
    data object OnSelectDisplayModelAreaClick : OnboardingAction
    data object OnNextButtonClick : OnboardingAction
    data object OnPreviousButtonClick : OnboardingAction
    data class OnOpenDocumentLauncherResult(val uri: Uri?) : OnboardingAction
}
