package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ModelSettings
import javax.inject.Inject

interface SaveModelSettingsUseCase {
    suspend operator fun invoke(modelSettings: ModelSettings)
}

class SaveModelSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveModelSettingsUseCase {
    override suspend operator fun invoke(modelSettings: ModelSettings) =
        userSettingsRepository.saveModelSettings(modelSettings)
}
