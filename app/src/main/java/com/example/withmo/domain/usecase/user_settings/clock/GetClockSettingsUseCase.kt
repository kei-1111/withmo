package com.example.withmo.domain.usecase.user_settings.clock

import com.example.withmo.domain.model.user_settings.ClockSettings
import kotlinx.coroutines.flow.Flow

interface GetClockSettingsUseCase {
    suspend operator fun invoke(): Flow<ClockSettings>
}
