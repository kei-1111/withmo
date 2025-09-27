package io.github.kei_1111.withmo.feature.setting.clock

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetClockSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveClockSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockSettingsViewModel @Inject constructor(
    private val getClockSettingsUseCase: GetClockSettingsUseCase,
    private val saveClockSettingsUseCase: SaveClockSettingsUseCase,
) : StatefulBaseViewModel<ClockSettingsViewModelState, ClockSettingsState, ClockSettingsAction, ClockSettingsEffect>() {

    override fun createInitialViewModelState() = ClockSettingsViewModelState()
    override fun createInitialState() = ClockSettingsState.Idle

    private val clockSettingsDataStream = getClockSettingsUseCase()

    init {
        viewModelScope.launch {
            updateViewModelState { copy(statusType = ClockSettingsViewModelState.StatusType.LOADING) }
            clockSettingsDataStream.collect { result ->
                result
                    .onSuccess { clockSettings ->
                        updateViewModelState {
                            copy(
                                statusType = ClockSettingsViewModelState.StatusType.STABLE,
                                clockSettings = clockSettings,
                                initialClockSettings = clockSettings,
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = ClockSettingsViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
                    }
            }
        }
    }

    override fun onAction(action: ClockSettingsAction) {
        when (action) {
            is ClockSettingsAction.OnIsClockShownSwitchChange -> {
                updateViewModelState {
                    val updatedClockSettings = clockSettings.copy(isClockShown = action.isClockShown)
                    copy(clockSettings = updatedClockSettings)
                }
            }

            is ClockSettingsAction.OnClockTypeRadioButtonClick -> {
                updateViewModelState {
                    val updatedClockSettings = clockSettings.copy(clockType = action.clockType)
                    copy(clockSettings = updatedClockSettings)
                }
            }

            is ClockSettingsAction.OnSaveButtonClick -> {
                viewModelScope.launch {
                    try {
                        saveClockSettingsUseCase(_viewModelState.value.clockSettings)
                        sendEffect(ClockSettingsEffect.NavigateBack)
                        sendEffect(ClockSettingsEffect.ShowToast("保存しました"))
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to save clock settings", e)
                        sendEffect(ClockSettingsEffect.ShowToast("保存に失敗しました"))
                    }
                }
            }

            is ClockSettingsAction.OnBackButtonClick -> {
                sendEffect(ClockSettingsEffect.NavigateBack)
            }
        }
    }

    private companion object {
        const val TAG = "ClockSettingsViewModel"
    }
}
