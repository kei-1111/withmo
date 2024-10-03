package com.example.withmo.domain.usecase

import com.example.withmo.domain.model.SortMode
import com.example.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveSortModeUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveSortModeUseCase {
    override suspend operator fun invoke(sortMode: SortMode) = userSettingsRepository.saveSortMode(sortMode)
}
