package io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path

import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetModelFilePathUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetModelFilePathUseCase {
    override operator fun invoke(): Flow<ModelFilePath> =
        userSettingsRepository.userSettings
            .map { it.modelFilePath }
            .distinctUntilChanged()
}
