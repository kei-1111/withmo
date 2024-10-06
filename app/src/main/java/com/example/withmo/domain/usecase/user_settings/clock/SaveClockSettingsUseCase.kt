package com.example.withmo.domain.usecase.user_settings.clock

import com.example.withmo.domain.model.user_settings.ClockSettings

interface SaveClockSettingsUseCase {
    suspend operator fun invoke(clockSettings: ClockSettings)
}
