package com.example.withmo.domain.usecase.user_settings.sort

import com.example.withmo.domain.model.user_settings.SortSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveSortSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveSortSettingsUseCase {
    override suspend operator fun invoke(
        sortSettings: SortSettings,
    ) = userSettingsRepository.saveSortSettings(sortSettings)
}
