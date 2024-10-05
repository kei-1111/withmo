package com.example.withmo.domain.usecase.user_settings

import com.example.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class GetUserSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetUserSettingsUseCase {
    override suspend operator fun invoke() = userSettingsRepository.userSettings
}
