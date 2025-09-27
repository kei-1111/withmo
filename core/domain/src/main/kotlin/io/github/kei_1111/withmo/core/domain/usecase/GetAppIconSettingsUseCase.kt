package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetAppIconSettingsUseCase {
    operator fun invoke(): Flow<Result<AppIconSettings>>
}

class GetAppIconSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetAppIconSettingsUseCase {
    override fun invoke(): Flow<Result<AppIconSettings>> =
        userSettingsRepository.userSettings
            .map { result ->
                result.map { it.appIconSettings }
            }
            .distinctUntilChanged()
}
