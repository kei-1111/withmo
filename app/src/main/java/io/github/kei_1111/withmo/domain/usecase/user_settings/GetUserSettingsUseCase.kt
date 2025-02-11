package io.github.kei_1111.withmo.domain.usecase.user_settings

import io.github.kei_1111.withmo.domain.model.user_settings.UserSettings
import kotlinx.coroutines.flow.Flow

interface GetUserSettingsUseCase {
    suspend operator fun invoke(): Flow<UserSettings>
}
