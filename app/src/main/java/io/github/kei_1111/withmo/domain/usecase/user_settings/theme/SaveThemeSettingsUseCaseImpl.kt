package io.github.kei_1111.withmo.domain.usecase.user_settings.theme

import io.github.kei_1111.withmo.domain.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveThemeSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveThemeSettingsUseCase {
    override suspend operator fun invoke(themeSettings: ThemeSettings) {
        userSettingsRepository.saveThemeSettings(themeSettings)
    }
}
