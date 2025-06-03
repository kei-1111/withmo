package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import javax.inject.Inject

interface SaveThemeSettingsUseCase {
    suspend operator fun invoke(themeSettings: ThemeSettings)
}

class SaveThemeSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveThemeSettingsUseCase {
    override suspend operator fun invoke(themeSettings: ThemeSettings) {
        userSettingsRepository.saveThemeSettings(themeSettings)
    }
}
