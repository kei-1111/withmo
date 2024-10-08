package com.example.withmo.domain.usecase.user_settings.app_icon

import com.example.withmo.domain.model.user_settings.AppIconSettings

interface SaveAppIconSettingsUseCase {
    suspend operator fun invoke(appIconSettings: AppIconSettings)
}
