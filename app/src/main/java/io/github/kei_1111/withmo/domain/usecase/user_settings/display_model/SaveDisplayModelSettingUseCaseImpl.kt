package io.github.kei_1111.withmo.domain.usecase.user_settings.display_model

import io.github.kei_1111.withmo.domain.model.user_settings.DisplayModelSetting
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class SaveDisplayModelSettingUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : SaveDisplayModelSettingUseCase {
    override suspend operator fun invoke(displayModelSetting: DisplayModelSetting) {
        userSettingsRepository.saveDisplayModelSetting(displayModelSetting)
    }
}
