package com.example.withmo.ui.screens.side_button

import com.example.withmo.domain.model.user_settings.SideButtonSettings

data class SideButtonSettingsUiState(
    val sideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val initialSideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val isSaveButtonEnabled: Boolean = false,
)
