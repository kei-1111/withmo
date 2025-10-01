package io.github.kei_1111.withmo.feature.onboarding.screens.welcome

import io.github.kei_1111.withmo.core.featurebase.Action

sealed interface WelcomeAction : Action {
    data object OnNextButtonClick : WelcomeAction
}
