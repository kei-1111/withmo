package com.example.withmo.domain.usecase.notification_usecase

import com.example.withmo.domain.model.user_settings.NotificationSettings
import kotlinx.coroutines.flow.Flow

interface GetNotificationSettingsUseCase {
    suspend operator fun invoke(): Flow<NotificationSettings>
}
