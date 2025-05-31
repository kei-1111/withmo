package io.github.kei_1111.withmo.core.domain.usecase.notification

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotificationSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetNotificationSettingsUseCase {
    override operator fun invoke(): Flow<NotificationSettings> =
        userSettingsRepository.userSettings
            .map { it.notificationSettings }
            .distinctUntilChanged()
}
