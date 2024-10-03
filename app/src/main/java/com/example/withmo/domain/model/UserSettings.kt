package com.example.withmo.domain.model

import com.example.withmo.ui.theme.UiConfig

data class UserSettings(
    val showClock: Boolean = true,
    val clockMode: ClockMode = ClockMode.TOP_DATE,
    val showNotificationAnimation: Boolean = false,
    val modelFileList: MutableList<ModelFile> = mutableListOf(),
    val appIconSize: Float = UiConfig.DefaultAppIconSize,
    val appIconPadding: Float = UiConfig.DefaultAppIconPadding,
    val showAppName: Boolean = true,
    val sortMode: SortMode = SortMode.ALPHABETICAL,
    val showScaleSliderButton: Boolean = true,
    val showSortButton: Boolean = true,
)
