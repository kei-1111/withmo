package io.github.kei_1111.withmo.core.domain.usecase.side_button

import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings

interface SaveSideButtonSettingsUseCase {
    suspend operator fun invoke(sideButtonSettings: SideButtonSettings)
}
