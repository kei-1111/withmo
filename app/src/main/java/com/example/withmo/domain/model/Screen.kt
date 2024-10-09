package com.example.withmo.domain.model

sealed interface Screen {
    data object Home : Screen
    data object Settings : Screen
    data object NotificationSettings : Screen
    data object ClockSettings : Screen
    data object AppIconSettings : Screen
    data object SideButtonSettings : Screen
    data object DisplayModelSetting : Screen
}
