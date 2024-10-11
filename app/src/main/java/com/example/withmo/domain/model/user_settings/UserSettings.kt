package com.example.withmo.domain.model.user_settings

data class UserSettings(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val clockSettings: ClockSettings = ClockSettings(),
    val appIconSettings: AppIconSettings = AppIconSettings(),
    val sortType: SortType = SortType.ALPHABETICAL,
    val sideButtonSettings: SideButtonSettings = SideButtonSettings(),
    val themeSettings: ThemeSettings = ThemeSettings(),
)
