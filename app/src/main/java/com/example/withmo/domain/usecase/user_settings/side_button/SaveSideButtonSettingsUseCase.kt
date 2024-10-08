package com.example.withmo.domain.usecase.user_settings.side_button

import com.example.withmo.domain.model.user_settings.SideButtonSettings

interface SaveSideButtonSettingsUseCase {
    suspend operator fun invoke(sideButtonSettings: SideButtonSettings)
}
