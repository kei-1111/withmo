package io.github.kei_1111.withmo.domain.usecase.user_settings.theme

import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings

interface SaveThemeSettingsUseCase {
    suspend operator fun invoke(themeSettings: ThemeSettings)
}
