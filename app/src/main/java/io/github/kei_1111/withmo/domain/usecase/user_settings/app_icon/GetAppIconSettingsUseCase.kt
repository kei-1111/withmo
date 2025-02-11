package io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon

import io.github.kei_1111.withmo.domain.model.user_settings.AppIconSettings
import kotlinx.coroutines.flow.Flow

interface GetAppIconSettingsUseCase {
    suspend operator fun invoke(): Flow<AppIconSettings>
}
