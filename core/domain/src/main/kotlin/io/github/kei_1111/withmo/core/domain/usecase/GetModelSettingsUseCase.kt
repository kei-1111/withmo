package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ModelSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetModelSettingsUseCase {
    operator fun invoke(): Flow<ModelSettings>
}

class GetModelSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetModelSettingsUseCase {
    override operator fun invoke(): Flow<ModelSettings> =
        userSettingsRepository.userSettings
            .map { it.modelSettings }
            .distinctUntilChanged()
}
