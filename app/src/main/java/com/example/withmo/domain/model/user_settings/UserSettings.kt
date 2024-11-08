package com.example.withmo.domain.model.user_settings

data class UserSettings(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val clockSettings: ClockSettings = ClockSettings(),
    val appIconSettings: AppIconSettings = AppIconSettings(),
    val sortSettings: SortSettings = SortSettings(),
    val sideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val themeSettings: ThemeSettings = ThemeSettings(),
)
