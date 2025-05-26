package io.github.kei_1111.withmo.ui.screens.theme_settings

import io.github.kei_1111.withmo.domain.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.ui.base.State

data class ThemeSettingsState(
    val themeSettings: ThemeSettings = ThemeSettings(),
    val initialThemeSettings: ThemeSettings = ThemeSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : State
