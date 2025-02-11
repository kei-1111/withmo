package io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon

import io.github.kei_1111.withmo.domain.model.user_settings.AppIconSettings

interface SaveAppIconSettingsUseCase {
    suspend operator fun invoke(appIconSettings: AppIconSettings)
}
