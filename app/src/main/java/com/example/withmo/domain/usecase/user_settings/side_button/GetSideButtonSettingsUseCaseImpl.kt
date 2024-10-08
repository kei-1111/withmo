package com.example.withmo.domain.usecase.user_settings.side_button

import com.example.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSideButtonSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetSideButtonSettingsUseCase {
    override suspend operator fun invoke() =
        userSettingsRepository.userSettings.map { it.sideButtonSettings }
}
