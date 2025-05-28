package io.github.kei_1111.withmo.core.domain.usecase.notification

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import javax.inject.Inject

class SaveNotificationSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveNotificationSettingsUseCase {
    override suspend operator fun invoke(notificationSettings: NotificationSettings) {
        userSettingsRepository.saveNotificationSettings(notificationSettings)
    }
}
