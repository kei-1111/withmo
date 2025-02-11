package io.github.kei_1111.withmo.domain.usecase.user_settings.clock

import io.github.kei_1111.withmo.domain.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveClockSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : io.github.kei_1111.withmo.domain.usecase.user_settings.clock.SaveClockSettingsUseCase {
    override suspend operator fun invoke(clockSettings: ClockSettings) {
        userSettingsRepository.saveClockSettings(clockSettings)
    }
}
