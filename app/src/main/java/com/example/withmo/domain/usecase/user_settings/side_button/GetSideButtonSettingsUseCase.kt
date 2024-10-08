package com.example.withmo.domain.usecase.user_settings.side_button

import com.example.withmo.domain.model.user_settings.SideButtonSettings
import kotlinx.coroutines.flow.Flow

interface GetSideButtonSettingsUseCase {
    suspend operator fun invoke(): Flow<SideButtonSettings>
}
