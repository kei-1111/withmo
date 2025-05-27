package io.github.kei_1111.withmo.ui.screens.app_icon_settings

import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings

data class AppIconSettingsState(
    val appIconSettings: AppIconSettings = AppIconSettings(),
    val initialAppIconSettings: AppIconSettings = AppIconSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : State
