package io.github.kei_1111.withmo.feature.setting.screen.side_button

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings

internal sealed interface SideButtonSettingsState : State {
    data object Idle : SideButtonSettingsState

    data object Loading : SideButtonSettingsState

    data class Stable(
        val sideButtonSettings: SideButtonSettings = SideButtonSettings(),
        val isSaveButtonEnabled: Boolean = false,
    ) : SideButtonSettingsState

    data class Error(val error: Throwable) : SideButtonSettingsState
}
