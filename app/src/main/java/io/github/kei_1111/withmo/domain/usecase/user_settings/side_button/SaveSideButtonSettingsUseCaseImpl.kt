package io.github.kei_1111.withmo.domain.usecase.user_settings.side_button

import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveSideButtonSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveSideButtonSettingsUseCase {
    override suspend operator fun invoke(sideButtonSettings: SideButtonSettings) {
        userSettingsRepository.saveSideButtonSettings(sideButtonSettings)
    }
}
