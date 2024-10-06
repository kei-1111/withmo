package com.example.withmo.domain.usecase.user_settings.clock

import com.example.withmo.domain.model.user_settings.ClockSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveClockSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveClockSettingsUseCase {
    override suspend operator fun invoke(clockSettings: ClockSettings) {
        userSettingsRepository.saveClockSettings(clockSettings)
    }
}
