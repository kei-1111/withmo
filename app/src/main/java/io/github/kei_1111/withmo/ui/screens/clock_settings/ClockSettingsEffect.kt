package io.github.kei_1111.withmo.ui.screens.clock_settings

import io.github.kei_1111.withmo.ui.base.Effect

sealed interface ClockSettingsEffect : Effect {
    data object NavigateBack : ClockSettingsEffect
    data class ShowToast(val message: String) : ClockSettingsEffect
}
