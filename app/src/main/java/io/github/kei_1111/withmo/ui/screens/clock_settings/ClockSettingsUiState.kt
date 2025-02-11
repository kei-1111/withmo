package io.github.kei_1111.withmo.ui.screens.clock_settings

import io.github.kei_1111.withmo.domain.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.ui.base.UiState

data class ClockSettingsUiState(
    val clockSettings: ClockSettings = ClockSettings(),
    val initialClockSettings: ClockSettings = ClockSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : UiState
