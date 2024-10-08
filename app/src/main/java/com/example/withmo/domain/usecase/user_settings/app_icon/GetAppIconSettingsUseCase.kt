package com.example.withmo.domain.usecase.user_settings.app_icon

import com.example.withmo.domain.model.user_settings.AppIconSettings
import kotlinx.coroutines.flow.Flow

interface GetAppIconSettingsUseCase {
    suspend operator fun invoke(): Flow<AppIconSettings>
}
