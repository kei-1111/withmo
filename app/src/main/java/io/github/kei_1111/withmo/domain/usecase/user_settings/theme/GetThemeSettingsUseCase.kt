package io.github.kei_1111.withmo.domain.usecase.user_settings.theme

import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import kotlinx.coroutines.flow.Flow

interface GetThemeSettingsUseCase {
    operator fun invoke(): Flow<ThemeSettings>
}
