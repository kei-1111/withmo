package com.example.withmo.domain.usecase.user_settings.clock

import com.example.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetClockSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetClockSettingsUseCase {
    override suspend operator fun invoke() = userSettingsRepository.userSettings.map { it.clockSettings }
}
