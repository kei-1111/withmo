package io.github.kei_1111.withmo.feature.onboarding.screen.finish

import io.github.kei_1111.withmo.core.featurebase.Action

internal sealed interface FinishAction : Action {
    data object OnBackButtonClick : FinishAction
    data object OnFinishButtonClick : FinishAction
}
