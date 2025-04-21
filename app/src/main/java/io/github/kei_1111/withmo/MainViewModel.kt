package io.github.kei_1111.withmo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.navigation.Screen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val oneTimeEventRepository: OneTimeEventRepository,
) : ViewModel() {
    var startScreen by mutableStateOf<Screen?>(null)
        private set

    init {
        loadStartScreen()
    }

    private fun loadStartScreen() {
        viewModelScope.launch {
            val isOnboardingShown = oneTimeEventRepository.isOnboardingFirstShown.first()
            startScreen = if (isOnboardingShown) {
                Screen.Home
            } else {
                Screen.Onboarding
            }
        }
    }
}
