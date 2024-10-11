package com.example.withmo.domain.usecase.user_settings.sort_mode

import com.example.withmo.domain.model.user_settings.SortType
import com.example.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveSortTypeUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveSortTypeUseCase {
    override suspend operator fun invoke(sortType: SortType) = userSettingsRepository.saveSortType(sortType)
}
