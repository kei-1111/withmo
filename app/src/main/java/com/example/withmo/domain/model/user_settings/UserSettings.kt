package com.example.withmo.domain.model.user_settings

import com.example.withmo.domain.model.SortMode

data class UserSettings(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val clockSettings: ClockSettings = ClockSettings(),
    val appIconSettings: AppIconSettings = AppIconSettings(),
    val sortMode: SortMode = SortMode.ALPHABETICAL,
    val sideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val themeSettings: ThemeSettings = ThemeSettings(),
)
