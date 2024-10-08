package com.example.withmo.domain.usecase.user_settings.side_button

import com.example.withmo.domain.model.user_settings.SideButtonSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveSideButtonSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveSideButtonSettingsUseCase {
    override suspend operator fun invoke(sideButtonSettings: SideButtonSettings) {
        userSettingsRepository.saveSideButtonSettings(sideButtonSettings)
    }
}
