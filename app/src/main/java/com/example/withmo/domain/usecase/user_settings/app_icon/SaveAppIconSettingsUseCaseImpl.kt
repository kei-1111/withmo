package com.example.withmo.domain.usecase.user_settings.app_icon

import com.example.withmo.domain.model.user_settings.AppIconSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveAppIconSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveAppIconSettingsUseCase {
    override suspend operator fun invoke(appIconSettings: AppIconSettings) {
        userSettingsRepository.saveAppIconSettings(appIconSettings)
    }
}
