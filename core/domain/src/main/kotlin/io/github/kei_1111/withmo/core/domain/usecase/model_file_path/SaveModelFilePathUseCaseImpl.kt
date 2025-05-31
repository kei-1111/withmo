package io.github.kei_1111.withmo.core.domain.usecase.model_file_path

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import javax.inject.Inject

class SaveModelFilePathUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveModelFilePathUseCase {
    override suspend operator fun invoke(modelFilePath: ModelFilePath) =
        userSettingsRepository.saveModelFilePath(modelFilePath)
}
