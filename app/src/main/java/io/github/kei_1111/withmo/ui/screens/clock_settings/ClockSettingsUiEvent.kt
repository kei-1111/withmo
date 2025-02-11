package io.github.kei_1111.withmo.ui.screens.clock_settings

import io.github.kei_1111.withmo.domain.model.user_settings.ClockType
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface ClockSettingsUiEvent : UiEvent {
    data class ChangeIsClockShown(
        val isClockShown: Boolean,
    ) : ClockSettingsUiEvent
    data class ChangeClockType(
        val clockType: ClockType,
    ) : ClockSettingsUiEvent
    data object Save : ClockSettingsUiEvent
    data object SaveSuccess : ClockSettingsUiEvent
    data object SaveFailure : ClockSettingsUiEvent
    data object NavigateToSettingsScreen : ClockSettingsUiEvent
}
