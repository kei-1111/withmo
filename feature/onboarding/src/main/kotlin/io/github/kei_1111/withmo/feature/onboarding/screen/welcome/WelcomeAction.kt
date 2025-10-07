package io.github.kei_1111.withmo.feature.onboarding.screen.welcome

import io.github.kei_1111.withmo.core.featurebase.Action

internal sealed interface WelcomeAction : Action {
    data object OnNextButtonClick : WelcomeAction
}
