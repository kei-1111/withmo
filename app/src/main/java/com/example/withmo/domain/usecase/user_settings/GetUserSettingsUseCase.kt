package com.example.withmo.domain.usecase.user_settings

import com.example.withmo.domain.model.user_settings.UserSettings
import kotlinx.coroutines.flow.Flow

interface GetUserSettingsUseCase {
    suspend operator fun invoke(): Flow<UserSettings>
}
