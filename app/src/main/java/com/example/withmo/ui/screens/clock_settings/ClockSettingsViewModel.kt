package com.example.withmo.ui.screens.clock_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.ClockMode
import com.example.withmo.domain.usecase.user_settings.clock.GetClockSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.clock.SaveClockSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockSettingsViewModel @Inject constructor(
    private val getClockSettingsUseCase: GetClockSettingsUseCase,
    private val saveClockSettingsUseCase: SaveClockSettingsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClockSettingsUiState())
    val uiState: StateFlow<ClockSettingsUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ClockSettingsUiEvent>()
    val uiEvent: SharedFlow<ClockSettingsUiEvent> = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            getClockSettingsUseCase().collect { clockSettings ->
                _uiState.update {
                    it.copy(
                        clockSettings = clockSettings,
                        initialClockSettings = clockSettings,
                    )
                }
            }
        }
    }

    fun changeIsClockShown(isClockShown: Boolean) {
        _uiState.update {
            it.copy(
                clockSettings = it.clockSettings.copy(
                    isClockShown = isClockShown,
                ),
                isSaveButtonEnabled = isClockShown != it.initialClockSettings.isClockShown,
            )
        }
    }

    fun changeClockMode(clockMode: ClockMode) {
        _uiState.update {
            it.copy(
                clockSettings = it.clockSettings.copy(
                    clockMode = clockMode,
                ),
                isSaveButtonEnabled = clockMode != it.initialClockSettings.clockMode,
            )
        }
    }

    fun saveClockSettings() {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                saveClockSettingsUseCase(uiState.value.clockSettings)
                _uiEvent.emit(ClockSettingsUiEvent.SaveSuccess)
            } catch (e: Exception) {
                Log.e("ClockSettingsViewModel", "Failed to save clock settings", e)
                _uiEvent.emit(ClockSettingsUiEvent.SaveFailure)
            }
        }
    }

    fun onEvent(event: ClockSettingsUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
