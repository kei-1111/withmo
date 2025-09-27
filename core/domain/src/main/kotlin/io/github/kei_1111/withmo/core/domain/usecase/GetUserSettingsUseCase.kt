package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetUserSettingsUseCase {
    operator fun invoke(): Flow<Result<UserSettings>>
}

class GetUserSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetUserSettingsUseCase {
    override operator fun invoke(): Flow<Result<UserSettings>> =
        userSettingsRepository.userSettings
}
