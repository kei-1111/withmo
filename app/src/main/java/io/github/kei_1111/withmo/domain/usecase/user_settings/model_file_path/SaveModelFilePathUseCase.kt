package io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path

import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath

interface SaveModelFilePathUseCase {
    suspend operator fun invoke(modelFilePath: ModelFilePath)
}
