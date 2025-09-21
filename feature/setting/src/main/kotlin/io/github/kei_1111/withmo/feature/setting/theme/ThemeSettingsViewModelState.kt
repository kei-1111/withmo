package io.github.kei_1111.withmo.feature.setting.theme

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings

data class ThemeSettingsViewModelState(
    val themeSettings: ThemeSettings = ThemeSettings(),
    val initialThemeSettings: ThemeSettings = ThemeSettings(),
) : ViewModelState<ThemeSettingsState> {
    override fun toState() = ThemeSettingsState(
        themeSettings = themeSettings,
        isSaveButtonEnabled = themeSettings != initialThemeSettings,
    )
}
