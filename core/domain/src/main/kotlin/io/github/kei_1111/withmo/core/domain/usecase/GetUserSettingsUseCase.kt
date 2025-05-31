package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import kotlinx.coroutines.flow.Flow

interface GetUserSettingsUseCase {
    operator fun invoke(): Flow<UserSettings>
}
