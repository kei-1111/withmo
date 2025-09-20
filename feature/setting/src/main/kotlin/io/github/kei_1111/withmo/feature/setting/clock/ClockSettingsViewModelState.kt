package io.github.kei_1111.withmo.feature.setting.clock

import io.github.kei_1111.withmo.core.featurebase.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings

data class ClockSettingsViewModelState(
    val clockSettings: ClockSettings = ClockSettings(),
    val initialClockSettings: ClockSettings = ClockSettings(),
) : ViewModelState<ClockSettingsState> {
    override fun toState() = ClockSettingsState(
        clockSettings = clockSettings,
        isSaveButtonEnabled = clockSettings != initialClockSettings,
    )
}
