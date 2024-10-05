package com.example.withmo.domain.usecase.user_settings.notification

import com.example.withmo.domain.model.user_settings.NotificationSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveNotificationSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveNotificationSettingsUseCase {
    override suspend operator fun invoke(notificationSettings: NotificationSettings) {
        userSettingsRepository.saveNotificationSettings(notificationSettings)
    }
}
