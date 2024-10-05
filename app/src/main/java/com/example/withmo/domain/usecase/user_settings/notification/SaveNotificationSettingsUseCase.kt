package com.example.withmo.domain.usecase.user_settings.notification

import com.example.withmo.domain.model.user_settings.NotificationSettings

interface SaveNotificationSettingsUseCase {
    suspend operator fun invoke(notificationSettings: NotificationSettings)
}
