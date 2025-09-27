package io.github.kei_1111.withmo.feature.setting.theme

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings

sealed interface ThemeSettingsState : State {
    data object Idle : ThemeSettingsState

    data object Loading : ThemeSettingsState

    data class Stable(
        val themeSettings: ThemeSettings = ThemeSettings(),
        val isSaveButtonEnabled: Boolean = false,
    ) : ThemeSettingsState

    data class Error(val error: Throwable) : ThemeSettingsState
}
