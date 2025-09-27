package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetNotificationSettingsUseCase {
    operator fun invoke(): Flow<Result<NotificationSettings>>
}

class GetNotificationSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetNotificationSettingsUseCase {
    override operator fun invoke(): Flow<Result<NotificationSettings>> =
        userSettingsRepository.userSettings
            .map { result ->
                result.map { it.notificationSettings }
            }
            .distinctUntilChanged()
}
