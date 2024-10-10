package com.example.withmo.domain.usecase.user_settings.theme

import com.example.withmo.domain.model.user_settings.ThemeSettings
import kotlinx.coroutines.flow.Flow

interface GetThemeSettingsUseCase {
    suspend operator fun invoke(): Flow<ThemeSettings>
}
