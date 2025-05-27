package io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path

import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import kotlinx.coroutines.flow.Flow

interface GetModelFilePathUseCase {
    operator fun invoke(): Flow<ModelFilePath>
}
