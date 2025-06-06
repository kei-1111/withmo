package io.github.kei_1111.withmo.feature.setting.clock

import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings

data class ClockSettingsState(
    val clockSettings: ClockSettings = ClockSettings(),
    val initialClockSettings: ClockSettings = ClockSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : State
