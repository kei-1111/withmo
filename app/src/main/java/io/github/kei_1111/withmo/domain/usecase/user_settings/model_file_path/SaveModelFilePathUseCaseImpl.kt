package io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path

import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveModelFilePathUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveModelFilePathUseCase {
    override suspend operator fun invoke(modelFilePath: ModelFilePath) =
        userSettingsRepository.saveModelFilePath(modelFilePath)
}
