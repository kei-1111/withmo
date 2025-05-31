package io.github.kei_1111.withmo.core.domain.usecase.notification

import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import kotlinx.coroutines.flow.Flow

interface GetNotificationSettingsUseCase {
    operator fun invoke(): Flow<NotificationSettings>
}
