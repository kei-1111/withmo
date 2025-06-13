package io.github.kei_1111.withmo.feature.onboarding

import android.net.Uri
import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.WithmoAppInfo

sealed interface OnboardingAction : Action {
    data class OnAppSearchQueryChange(val query: String) : OnboardingAction
    data class OnAllAppListAppClick(val withmoAppInfo: WithmoAppInfo) : OnboardingAction
    data class OnFavoriteAppListAppClick(val withmoAppInfo: WithmoAppInfo) : OnboardingAction
    data object OnSelectDisplayModelAreaClick : OnboardingAction
    data object OnNextButtonClick : OnboardingAction
    data object OnPreviousButtonClick : OnboardingAction
    data class OnOpenDocumentLauncherResult(val uri: Uri?) : OnboardingAction
}
