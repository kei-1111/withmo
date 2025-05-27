package io.github.kei_1111.withmo.ui.screens.side_button_settings

import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.domain.model.user_settings.SideButtonSettings

data class SideButtonSettingsState(
    val sideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val initialSideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : State
