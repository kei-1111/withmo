package com.example.withmo.ui.screens.setting

import com.example.withmo.domain.model.SettingMode
import com.example.withmo.domain.model.UserSetting

data class SettingScreenUiState(
    val settingMode: SettingMode = SettingMode.HOME,
    val showNotificationCheckDialog: Boolean = false,
    val showFileAccessCheckDialog: Boolean = false,
    val currentUserSetting: UserSetting = UserSetting(),
)
