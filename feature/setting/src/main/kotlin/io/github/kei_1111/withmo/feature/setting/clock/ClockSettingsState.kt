package io.github.kei_1111.withmo.feature.setting.clock

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings

sealed interface ClockSettingsState : State {
    data object Idle : ClockSettingsState

    data object Loading : ClockSettingsState

    data class Stable(
        val clockSettings: ClockSettings = ClockSettings(),
        val isSaveButtonEnabled: Boolean = false,
    ) : ClockSettingsState

    data class Error(val error: Throwable) : ClockSettingsState
}
