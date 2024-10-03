package com.example.withmo.domain.usecase

import com.example.withmo.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface GetUserSettingsUseCase {
    suspend operator fun invoke(): Flow<UserSettings>
}
