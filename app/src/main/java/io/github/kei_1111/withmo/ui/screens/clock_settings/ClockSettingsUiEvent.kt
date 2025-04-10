package io.github.kei_1111.withmo.ui.screens.clock_settings

import io.github.kei_1111.withmo.domain.model.user_settings.ClockType
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface ClockSettingsUiEvent : UiEvent {
    data class OnIsClockShownSwitchChange(val isClockShown: Boolean) : ClockSettingsUiEvent
    data class OnClockTypeRadioButtonClick(val clockType: ClockType) : ClockSettingsUiEvent
    data object OnSaveButtonClick : ClockSettingsUiEvent
    data object OnNavigateToSettingsScreenButtonClick : ClockSettingsUiEvent
    data object OnBackButtonClick : ClockSettingsUiEvent
}
