package io.github.kei_1111.withmo.domain.usecase.user_settings.clock

import io.github.kei_1111.withmo.domain.model.user_settings.ClockSettings
import kotlinx.coroutines.flow.Flow

interface GetClockSettingsUseCase {
    suspend operator fun invoke(): Flow<ClockSettings>
}
