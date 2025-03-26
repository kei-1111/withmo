package io.github.kei_1111.withmo.domain.usecase.user_settings.display_model

import io.github.kei_1111.withmo.domain.model.user_settings.DisplayModelSetting
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDisplayModelSettingUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetDisplayModelSettingUseCase {
    override suspend operator fun invoke(): Flow<DisplayModelSetting> =
        userSettingsRepository.userSettings.map { it.displayModelSetting }
}
