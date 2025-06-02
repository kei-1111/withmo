package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import javax.inject.Inject

interface SaveSortSettingsUseCase {
    suspend operator fun invoke(sortSettings: SortSettings)
}

class SaveSortSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveSortSettingsUseCase {
    override suspend operator fun invoke(
        sortSettings: SortSettings,
    ) = userSettingsRepository.saveSortSettings(sortSettings)
}
