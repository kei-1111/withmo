package io.github.kei_1111.withmo.core.domain.usecase.app_icon

import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings

interface SaveAppIconSettingsUseCase {
    suspend operator fun invoke(appIconSettings: AppIconSettings)
}
