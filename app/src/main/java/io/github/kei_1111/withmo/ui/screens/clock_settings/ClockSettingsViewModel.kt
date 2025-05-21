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
) : BaseViewModel<ClockSettingsState, ClockSettingsAction, ClockSettingsEffect>() {

    override fun createInitialState(): ClockSettingsState = ClockSettingsState()

    init {
        observeClockSettings()
    }

    private fun observeClockSettings() {
        viewModelScope.launch {
            getClockSettingsUseCase().collect { clockSettings ->
                _state.update {
                    it.copy(
                        clockSettings = clockSettings,
                        initialClockSettings = clockSettings,
                    )
                }
            }
        }
    }

    fun changeIsClockShown(isClockShown: Boolean) {
        _state.update {
            it.copy(
                clockSettings = it.clockSettings.copy(
                    isClockShown = isClockShown,
                ),
                isSaveButtonEnabled = isClockShown != it.initialClockSettings.isClockShown,
            )
        }
    }

    fun changeClockType(clockType: ClockType) {
        _state.update {
            it.copy(
                clockSettings = it.clockSettings.copy(
                    clockType = clockType,
                ),
                isSaveButtonEnabled = clockType != it.initialClockSettings.clockType,
            )
        }
    }

    fun saveClockSettings(
        onSaveSuccess: () -> Unit,
        onSaveFailure: () -> Unit,
    ) {
        _state.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                saveClockSettingsUseCase(state.value.clockSettings)
                onSaveSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save clock settings", e)
                onSaveFailure()
            }
        }
    }

    override fun onAction(action: ClockSettingsAction) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }

    private companion object {
        const val TAG = "ClockSettingsViewModel"
    }
}
