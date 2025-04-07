package io.github.kei_1111.withmo.ui.screens.clock_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.model.user_settings.ClockType
import io.github.kei_1111.withmo.domain.usecase.user_settings.clock.GetClockSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.clock.SaveClockSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockSettingsViewModel @Inject constructor(
    private val getClockSettingsUseCase: GetClockSettingsUseCase,
    private val saveClockSettingsUseCase: SaveClockSettingsUseCase,
) : BaseViewModel<ClockSettingsUiState, ClockSettingsUiEvent>() {

    override fun createInitialState(): ClockSettingsUiState = ClockSettingsUiState()

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

    fun changeClockType(clockType: ClockType) {
        _uiState.update {
            it.copy(
                clockSettings = it.clockSettings.copy(
                    clockType = clockType,
                ),
                isSaveButtonEnabled = clockType != it.initialClockSettings.clockType,
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
}
