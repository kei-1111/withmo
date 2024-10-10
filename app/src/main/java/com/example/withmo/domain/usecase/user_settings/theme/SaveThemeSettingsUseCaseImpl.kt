package com.example.withmo.domain.usecase.user_settings.theme

import com.example.withmo.domain.model.user_settings.ThemeSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveThemeSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveThemeSettingsUseCase {
    override suspend operator fun invoke(themeSettings: ThemeSettings) {
        userSettingsRepository.saveThemeSettings(themeSettings)
    }
}
