package io.github.kei_1111.withmo.feature.setting.side_button

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings

data class SideButtonSettingsViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val sideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val initialSideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val error: Throwable? = null,
) : ViewModelState<SideButtonSettingsState> {

    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> SideButtonSettingsState.Idle

        StatusType.LOADING -> SideButtonSettingsState.Loading

        StatusType.STABLE -> SideButtonSettingsState.Stable(
            sideButtonSettings = sideButtonSettings,
            isSaveButtonEnabled = sideButtonSettings != initialSideButtonSettings,
        )

        StatusType.ERROR -> SideButtonSettingsState.Error(error ?: Throwable("Unknown error"))
    }
}
