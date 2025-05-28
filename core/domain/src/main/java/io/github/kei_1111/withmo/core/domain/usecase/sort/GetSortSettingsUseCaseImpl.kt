package io.github.kei_1111.withmo.core.domain.usecase.sort

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSortSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetSortSettingsUseCase {
    override operator fun invoke() =
        userSettingsRepository.userSettings
            .map { it.sortSettings }
            .distinctUntilChanged()
}
