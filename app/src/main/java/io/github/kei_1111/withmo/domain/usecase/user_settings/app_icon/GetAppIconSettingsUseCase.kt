package io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon

import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import kotlinx.coroutines.flow.Flow

interface GetAppIconSettingsUseCase {
    operator fun invoke(): Flow<AppIconSettings>
}
