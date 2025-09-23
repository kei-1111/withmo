package io.github.kei_1111.withmo.feature.setting.app_icon

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings

data class AppIconSettingsState(
    val appIconSettings: AppIconSettings = AppIconSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : State
