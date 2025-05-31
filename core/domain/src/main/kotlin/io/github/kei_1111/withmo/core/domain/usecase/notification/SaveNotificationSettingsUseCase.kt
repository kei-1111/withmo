package io.github.kei_1111.withmo.core.domain.usecase.notification

import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings

interface SaveNotificationSettingsUseCase {
    suspend operator fun invoke(notificationSettings: NotificationSettings)
}
