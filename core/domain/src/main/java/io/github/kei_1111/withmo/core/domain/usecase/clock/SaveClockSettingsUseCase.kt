package io.github.kei_1111.withmo.core.domain.usecase.clock

import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings

interface SaveClockSettingsUseCase {
    suspend operator fun invoke(clockSettings: ClockSettings)
}
