package io.github.kei_1111.withmo.feature.onboarding.finish

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.MarkOnboardingShownUseCase
import io.github.kei_1111.withmo.core.featurebase.stateless.StatelessBaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishViewModel @Inject constructor(
    private val markOnboardingShownUseCase: MarkOnboardingShownUseCase,
) : StatelessBaseViewModel<FinishAction, FinishEffect>() {
    override fun onAction(action: FinishAction) {
        when (action) {
            is FinishAction.OnBackButtonClick -> {
                sendEffect(FinishEffect.NavigateBack)
            }

            is FinishAction.OnFinishButtonClick -> {
                viewModelScope.launch {
                    markOnboardingShownUseCase()
                }
                sendEffect(FinishEffect.NavigateHome)
            }
        }
    }

    private companion object {
        const val TAG = "FinishViewModel"
    }
}
