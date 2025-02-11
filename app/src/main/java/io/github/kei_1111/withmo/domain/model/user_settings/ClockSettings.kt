package io.github.kei_1111.withmo.domain.model.user_settings

data class ClockSettings(
    val isClockShown: Boolean = true,
    val clockType: ClockType = ClockType.TOP_DATE,
)

enum class ClockType {
    TOP_DATE,
    HORIZONTAL_DATE,
}
