package com.example.withmo.ui.screens.clock_settings

import com.example.withmo.domain.model.ClockMode
import com.example.withmo.ui.base.UiEvent

sealed interface ClockSettingsUiEvent : UiEvent {
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
