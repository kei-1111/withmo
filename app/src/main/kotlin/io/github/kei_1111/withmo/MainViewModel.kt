package io.github.kei_1111.withmo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetOnboardingStatusUseCase
import io.github.kei_1111.withmo.core.ui.navigation.Home
import io.github.kei_1111.withmo.core.ui.navigation.NavigationRoute
import io.github.kei_1111.withmo.core.ui.navigation.OnboardingGraph
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
) : ViewModel() {
    var startDestination by mutableStateOf<NavigationRoute?>(null)
        private set

    init {
        loadStartDestination()
    }

    private fun loadStartDestination() {
        viewModelScope.launch {
            val isOnboardingShown = getOnboardingStatusUseCase()
            startDestination = if (isOnboardingShown) {
                Home
            } else {
                OnboardingGraph
            }
        }
    }
}
