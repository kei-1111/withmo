package io.github.kei_1111.withmo.domain.usecase.user_settings.side_button

import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
import kotlinx.coroutines.flow.Flow

interface GetSideButtonSettingsUseCase {
    operator fun invoke(): Flow<SideButtonSettings>
}
