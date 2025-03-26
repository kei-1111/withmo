package io.github.kei_1111.withmo.domain.usecase.user_settings.display_model

import io.github.kei_1111.withmo.domain.model.user_settings.DisplayModelSetting

interface SaveDisplayModelSettingUseCase {
    suspend operator fun invoke(displayModelSetting: DisplayModelSetting)
}
