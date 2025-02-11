package io.github.kei_1111.withmo.domain.usecase.user_settings.notification

import io.github.kei_1111.withmo.domain.model.user_settings.NotificationSettings
import kotlinx.coroutines.flow.Flow

interface GetNotificationSettingsUseCase {
    suspend operator fun invoke(): Flow<NotificationSettings>
}
