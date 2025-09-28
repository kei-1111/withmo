package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetClockSettingsUseCase {
    operator fun invoke(): Flow<Result<ClockSettings>>
}

class GetClockSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetClockSettingsUseCase {
    override operator fun invoke(): Flow<Result<ClockSettings>> =
        userSettingsRepository.userSettings
            .map { result ->
                result.map { it.clockSettings }
            }
            .distinctUntilChanged()
}
