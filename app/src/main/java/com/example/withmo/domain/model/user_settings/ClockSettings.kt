package com.example.withmo.domain.model.user_settings

import com.example.withmo.domain.model.ClockMode

data class ClockSettings(
    val isClockShown: Boolean = true,
    val clockMode: ClockMode = ClockMode.TOP_DATE,
)
