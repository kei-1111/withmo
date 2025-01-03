package com.example.withmo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.repository.AppInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appInfoRepository: AppInfoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AppUiState>(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        splashScreenDuration()
        isFirstLogin()
    }

    private fun isFirstLogin() {
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { appInfoList ->
                _uiState.update {
                    it.copy(isFirstLogin = appInfoList.isEmpty())
                }
            }
        }
    }

    private fun splashScreenDuration() {
        viewModelScope.launch {
            delay(SplashScreenDuration)
            _uiState.update {
                it.copy(isFinishSplashScreen = true)
            }
        }
    }

    companion object {
        private const val SplashScreenDuration = 4000L
    }
}
