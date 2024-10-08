package com.example.withmo.ui.screens.app_icon_settings

import com.example.withmo.domain.model.user_settings.AppIconSettings

data class AppIconSettingsUiState(
    val appIconSettings: AppIconSettings = AppIconSettings(),
    val initialAppIconSettings: AppIconSettings = AppIconSettings(),
    val isSaveButtonEnabled: Boolean = false,
)
