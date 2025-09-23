package io.github.kei_1111.withmo.feature.onboarding.welcome

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.featurebase.stateless.StatelessBaseViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : StatelessBaseViewModel<WelcomeAction, WelcomeEffect>() {
    override fun onAction(action: WelcomeAction) {
        when (action) {
            is WelcomeAction.OnNextButtonClick -> {
                sendEffect(WelcomeEffect.NavigateSelectFavoriteApp)
            }
        }
    }
}
