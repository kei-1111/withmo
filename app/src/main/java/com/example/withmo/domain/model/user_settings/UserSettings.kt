package com.example.withmo.domain.model.user_settings

import com.example.withmo.domain.model.ClockMode
import com.example.withmo.domain.model.SortMode
import com.example.withmo.ui.theme.UiConfig

data class UserSettings(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val showClock: Boolean = true,
    val clockMode: ClockMode = ClockMode.TOP_DATE,
    val appIconSize: Float = UiConfig.DefaultAppIconSize,
    val appIconPadding: Float = UiConfig.DefaultAppIconPadding,
    val showAppName: Boolean = true,
    val sortMode: SortMode = SortMode.ALPHABETICAL,
    val showScaleSliderButton: Boolean = true,
    val showSortButton: Boolean = true,
)
