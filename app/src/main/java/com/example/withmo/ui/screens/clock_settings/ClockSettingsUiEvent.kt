package com.example.withmo.ui.screens.clock_settings

import com.example.withmo.domain.model.ClockMode

sealed interface ClockSettingsUiEvent {
    data class ChangeIsClockShown(
        val isClockShown: Boolean,
    ) : ClockSettingsUiEvent
    data class ChangeClockMode(
        val clockMode: ClockMode,
    ) : ClockSettingsUiEvent
    data object Save : ClockSettingsUiEvent
    data object SaveSuccess : ClockSettingsUiEvent
    data object SaveFailure : ClockSettingsUiEvent
    data object NavigateToSettingsScreen : ClockSettingsUiEvent
}
