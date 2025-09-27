package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetSortSettingsUseCase {
    operator fun invoke(): Flow<Result<SortSettings>>
}

class GetSortSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetSortSettingsUseCase {
    override operator fun invoke(): Flow<Result<SortSettings>> =
        userSettingsRepository.userSettings
            .map { it.sortSettings }
            .distinctUntilChanged()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
