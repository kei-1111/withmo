package io.github.kei_1111.withmo.ui.screens.clock_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.domain.usecase.user_settings.clock.GetClockSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.clock.SaveClockSettingsUseCase
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
                updateState {
                    copy(
                        clockSettings = clockSettings,
                        initialClockSettings = clockSettings,
                    )
                }
            }
        }
    }

    private fun saveClockSettings() {
        updateState { copy(isSaveButtonEnabled = false) }
        viewModelScope.launch {
            try {
                saveClockSettingsUseCase(state.value.clockSettings)
                sendEffect(ClockSettingsEffect.NavigateBack)
                sendEffect(ClockSettingsEffect.ShowToast("保存しました"))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save clock settings", e)
                sendEffect(ClockSettingsEffect.ShowToast("保存に失敗しました"))
            }
        }
    }

    override fun onAction(action: ClockSettingsAction) {
        when (action) {
            is ClockSettingsAction.OnIsClockShownSwitchChange -> {
                updateState {
                    val updatedClockSettings = clockSettings.copy(isClockShown = action.isClockShown)
                    copy(
                        clockSettings = updatedClockSettings,
                        isSaveButtonEnabled = updatedClockSettings != initialClockSettings,
                    )
                }
            }

            is ClockSettingsAction.OnClockTypeRadioButtonClick -> {
                updateState {
                    val updatedClockSettings = clockSettings.copy(clockType = action.clockType)
                    copy(
                        clockSettings = updatedClockSettings,
                        isSaveButtonEnabled = updatedClockSettings != initialClockSettings,
                    )
                }
            }

            is ClockSettingsAction.OnSaveButtonClick -> saveClockSettings()

            is ClockSettingsAction.OnBackButtonClick -> sendEffect(ClockSettingsEffect.NavigateBack)
        }
    }

    private companion object {
        const val TAG = "ClockSettingsViewModel"
    }
}
