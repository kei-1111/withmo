package com.example.withmo.ui.screens.setting

import com.example.withmo.domain.model.SettingMode
import com.example.withmo.domain.model.user_settings.UserSettings

data class SettingScreenUiState(
    val settingMode: SettingMode = SettingMode.HOME,
    val showNotificationCheckDialog: Boolean = false,
    val showFileAccessCheckDialog: Boolean = false,
    val currentUserSettings: UserSettings = UserSettings(),
)
