package io.github.kei_1111.withmo.feature.setting.theme

import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings

data class ThemeSettingsState(
    val themeSettings: ThemeSettings = ThemeSettings(),
    val initialThemeSettings: ThemeSettings = ThemeSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : State
