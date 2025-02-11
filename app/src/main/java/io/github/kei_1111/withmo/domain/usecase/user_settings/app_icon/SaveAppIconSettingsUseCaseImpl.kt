package io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon

import io.github.kei_1111.withmo.domain.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveAppIconSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveAppIconSettingsUseCase {
    override suspend operator fun invoke(appIconSettings: AppIconSettings) {
        userSettingsRepository.saveAppIconSettings(appIconSettings)
    }
}
