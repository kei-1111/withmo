package io.github.kei_1111.withmo.ui.screens.app_icon_settings

import io.github.kei_1111.withmo.domain.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.ui.base.State

data class AppIconSettingsState(
    val appIconSettings: AppIconSettings = AppIconSettings(),
    val initialAppIconSettings: AppIconSettings = AppIconSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : State
