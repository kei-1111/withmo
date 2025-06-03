package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import javax.inject.Inject

interface SaveAppIconSettingsUseCase {
    suspend operator fun invoke(appIconSettings: AppIconSettings)
}

class SaveAppIconSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveAppIconSettingsUseCase {
    override suspend operator fun invoke(appIconSettings: AppIconSettings) {
        userSettingsRepository.saveAppIconSettings(appIconSettings)
    }
}
