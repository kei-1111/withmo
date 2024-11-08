package com.example.withmo.domain.usecase.user_settings.sort

import com.example.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSortSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetSortSettingsUseCase {
    override suspend operator fun invoke() =
        userSettingsRepository.userSettings.map { it.sortSettings }
}
