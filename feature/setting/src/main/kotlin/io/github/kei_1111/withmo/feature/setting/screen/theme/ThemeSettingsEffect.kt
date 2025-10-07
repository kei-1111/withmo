package io.github.kei_1111.withmo.feature.setting.screen.theme

import io.github.kei_1111.withmo.core.featurebase.Effect

internal sealed interface ThemeSettingsEffect : Effect {
    data object NavigateBack : ThemeSettingsEffect
    data class ShowToast(val message: String) : ThemeSettingsEffect
}
