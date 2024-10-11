package com.example.withmo.domain.model.user_settings

data class ClockSettings(
    val isClockShown: Boolean = true,
    val clockType: ClockType = ClockType.TOP_DATE,
)

enum class ClockType {
    TOP_DATE,
    HORIZONTAL_DATE,
}
