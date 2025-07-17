package io.github.kei_1111.withmo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetOnboardingStatusUseCase
import io.github.kei_1111.withmo.navigation.Screen
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
) : ViewModel() {
    var startScreen by mutableStateOf<Screen?>(null)
        private set

    init {
        loadStartScreen()
    }

    private fun loadStartScreen() {
        viewModelScope.launch {
            val isOnboardingShown = getOnboardingStatusUseCase()
            startScreen = if (isOnboardingShown) {
                Screen.Home
            } else {
                Screen.Onboarding
            }
        }
    }
}
