package io.github.kei_1111.withmo.ui.screens.theme_settings

import io.github.kei_1111.withmo.ui.base.Effect

sealed interface ThemeSettingsEffect : Effect {
    data object NavigateBack : ThemeSettingsEffect
    data class ShowToast(val message: String) : ThemeSettingsEffect
}
