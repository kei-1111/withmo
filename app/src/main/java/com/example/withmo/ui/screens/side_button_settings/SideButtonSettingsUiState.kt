package com.example.withmo.ui.screens.side_button_settings

import com.example.withmo.domain.model.user_settings.SideButtonSettings
import com.example.withmo.ui.base.UiState

data class SideButtonSettingsUiState(
    val sideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val initialSideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : UiState
