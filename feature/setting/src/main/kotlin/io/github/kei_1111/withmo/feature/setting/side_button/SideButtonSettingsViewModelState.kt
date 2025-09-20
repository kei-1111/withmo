package io.github.kei_1111.withmo.feature.setting.side_button

import io.github.kei_1111.withmo.core.featurebase.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings

data class SideButtonSettingsViewModelState(
    val sideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val initialSideButtonSettings: SideButtonSettings = SideButtonSettings(),
) : ViewModelState<SideButtonSettingsState> {
    override fun toState() = SideButtonSettingsState(
        sideButtonSettings = sideButtonSettings,
        isSaveButtonEnabled = sideButtonSettings != initialSideButtonSettings,
    )
}
