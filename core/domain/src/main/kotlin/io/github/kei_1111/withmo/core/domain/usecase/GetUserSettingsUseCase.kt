package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetUserSettingsUseCase {
    operator fun invoke(): Flow<Result<UserSettings>>
}

class GetUserSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetUserSettingsUseCase {
    override operator fun invoke(): Flow<Result<UserSettings>> =
        userSettingsRepository.userSettings
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
