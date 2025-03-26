package io.github.kei_1111.withmo.domain.usecase.user_settings.display_model

import io.github.kei_1111.withmo.domain.model.user_settings.DisplayModelSetting
import io.github.kei_1111.withmo.domain.model.user_settings.NotificationSettings
import kotlinx.coroutines.flow.Flow

interface GetDisplayModelSettingUseCase {
    suspend operator fun invoke(): Flow<DisplayModelSetting>
}
