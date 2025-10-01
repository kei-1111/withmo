package io.github.kei_1111.withmo.feature.onboarding.screens.welcome

import io.github.kei_1111.withmo.core.featurebase.Effect

internal sealed interface WelcomeEffect : Effect {
    data object NavigateSelectFavoriteApp : WelcomeEffect
}
