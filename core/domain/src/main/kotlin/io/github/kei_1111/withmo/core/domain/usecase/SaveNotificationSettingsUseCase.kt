package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import javax.inject.Inject

interface SaveNotificationSettingsUseCase {
    suspend operator fun invoke(notificationSettings: NotificationSettings)
}

class SaveNotificationSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveNotificationSettingsUseCase {
    override suspend operator fun invoke(notificationSettings: NotificationSettings) {
        userSettingsRepository.saveNotificationSettings(notificationSettings)
    }
}
