package com.example.withmo.domain.usecase.user_settings.theme

import com.example.withmo.domain.model.user_settings.ThemeSettings

interface SaveThemeSettingsUseCase {
    suspend operator fun invoke(themeSettings: ThemeSettings)
}
