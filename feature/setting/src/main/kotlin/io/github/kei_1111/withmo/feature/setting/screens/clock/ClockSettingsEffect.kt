package io.github.kei_1111.withmo.feature.setting.screens.clock

import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface ClockSettingsEffect : Effect {
    data object NavigateBack : ClockSettingsEffect
    data class ShowToast(val message: String) : ClockSettingsEffect
}
