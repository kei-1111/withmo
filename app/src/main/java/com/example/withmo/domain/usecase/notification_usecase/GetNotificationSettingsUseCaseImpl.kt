package com.example.withmo.domain.usecase.notification_usecase

import com.example.withmo.domain.model.user_settings.NotificationSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotificationSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetNotificationSettingsUseCase {
    override suspend operator fun invoke(): Flow<NotificationSettings> =
        userSettingsRepository.userSettings.map { it.notificationSettings }
}
