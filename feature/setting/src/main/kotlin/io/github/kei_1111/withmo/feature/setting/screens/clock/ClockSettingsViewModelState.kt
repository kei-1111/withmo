package io.github.kei_1111.withmo.feature.setting.screens.clock

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings

internal data class ClockSettingsViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val clockSettings: ClockSettings = ClockSettings(),
    val initialClockSettings: ClockSettings = ClockSettings(),
    val error: Throwable? = null,
) : ViewModelState<ClockSettingsState> {

    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> ClockSettingsState.Idle

        StatusType.LOADING -> ClockSettingsState.Loading

        StatusType.STABLE -> ClockSettingsState.Stable(
            clockSettings = clockSettings,
            isSaveButtonEnabled = clockSettings != initialClockSettings,
        )

        StatusType.ERROR -> ClockSettingsState.Error(error ?: Throwable("Unknown error"))
    }
}
