package io.github.kei_1111.withmo.feature.onboarding.screen.welcome

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.featurebase.stateless.StatelessBaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class WelcomeViewModel @Inject constructor() : StatelessBaseViewModel<WelcomeAction, WelcomeEffect>() {
    override fun onAction(action: WelcomeAction) {
        when (action) {
            is WelcomeAction.OnNextButtonClick -> {
                sendEffect(WelcomeEffect.NavigateSelectFavoriteApp)
            }
        }
    }

    private companion object {
        const val TAG = "WelcomeViewModel"
    }
}
