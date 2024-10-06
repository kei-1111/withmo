package com.example.withmo.ui.screens.clock_settings

import com.example.withmo.domain.model.user_settings.ClockSettings

data class ClockSettingsUiState(
    val clockSettings: ClockSettings = ClockSettings(),
    val initialClockSettings: ClockSettings = ClockSettings(),
    val isSaveButtonEnabled: Boolean = false,
)
