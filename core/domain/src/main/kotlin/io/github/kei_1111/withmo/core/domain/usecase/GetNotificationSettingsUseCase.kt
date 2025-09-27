package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
            .map { it.notificationSettings }
            .distinctUntilChanged()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
