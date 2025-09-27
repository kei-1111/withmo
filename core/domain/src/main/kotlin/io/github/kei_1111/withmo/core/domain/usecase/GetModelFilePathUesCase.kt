package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetModelFilePathUseCase {
    operator fun invoke(): Flow<Result<ModelFilePath>>
}

class GetModelFilePathUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetModelFilePathUseCase {
    override operator fun invoke(): Flow<Result<ModelFilePath>> =
        userSettingsRepository.userSettings
            .map { it.modelFilePath }
            .distinctUntilChanged()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
