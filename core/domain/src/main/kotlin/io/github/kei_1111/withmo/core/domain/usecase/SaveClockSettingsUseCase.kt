package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import javax.inject.Inject

interface SaveClockSettingsUseCase {
    suspend operator fun invoke(clockSettings: ClockSettings)
}

class SaveClockSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveClockSettingsUseCase {
    override suspend operator fun invoke(clockSettings: ClockSettings) {
        userSettingsRepository.saveClockSettings(clockSettings)
    }
}
