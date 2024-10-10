package com.example.withmo.ui.screens.theme_settings

import com.example.withmo.domain.model.user_settings.ThemeSettings
import com.example.withmo.ui.base.UiState

data class ThemeSettingsUiState(
    val themeSettings: ThemeSettings = ThemeSettings(),
    val initialThemeSettings: ThemeSettings = ThemeSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : UiState
