package io.github.kei_1111.withmo.domain.usecase.user_settings.side_button

import io.github.kei_1111.withmo.domain.model.user_settings.SideButtonSettings

interface SaveSideButtonSettingsUseCase {
    suspend operator fun invoke(sideButtonSettings: SideButtonSettings)
}
