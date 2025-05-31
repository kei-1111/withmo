package io.github.kei_1111.withmo.core.domain.usecase.side_button

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
import javax.inject.Inject

class SaveSideButtonSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveSideButtonSettingsUseCase {
    override suspend operator fun invoke(sideButtonSettings: SideButtonSettings) {
        userSettingsRepository.saveSideButtonSettings(sideButtonSettings)
    }
}
