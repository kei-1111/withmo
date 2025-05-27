package io.github.kei_1111.withmo.domain.usecase.user_settings.notification

import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveNotificationSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveNotificationSettingsUseCase {
    override suspend operator fun invoke(notificationSettings: NotificationSettings) {
        userSettingsRepository.saveNotificationSettings(notificationSettings)
    }
}
