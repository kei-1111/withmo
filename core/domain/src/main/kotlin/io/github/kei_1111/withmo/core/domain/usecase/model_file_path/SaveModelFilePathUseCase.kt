package io.github.kei_1111.withmo.core.domain.usecase.model_file_path

import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath

interface SaveModelFilePathUseCase {
    suspend operator fun invoke(modelFilePath: ModelFilePath)
}
