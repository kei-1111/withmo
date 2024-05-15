package com.example.withmo.domain.model

data class UserSetting(
    val showClock: Boolean = true,
    val clockMode: ClockMode = ClockMode.TOP_DATE,
    val showNotificationAnimation: Boolean = false,
    val modelFileList: MutableList<ModelFile> = mutableListOf(),
    val appIconSize: Float = 48f,
    val appIconPadding: Float = 10f,
    val showAppName: Boolean = true,
    val sortMode: SortMode = SortMode.ALPHABETICAL,
    val showScaleSliderButton: Boolean = true,
    val showSortButton: Boolean = true,
)
