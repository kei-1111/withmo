package io.github.kei_1111.withmo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
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
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
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

    private companion object {
        const val TAG = "AppViewModel"
    }
}
