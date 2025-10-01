package io.github.kei_1111.withmo.feature.setting.screens.theme

import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

internal sealed interface ThemeSettingsAction : Action {
    data class OnThemeTypeRadioButtonClick(val themeType: ThemeType) : ThemeSettingsAction
    data object OnSaveButtonClick : ThemeSettingsAction
    data object OnBackButtonClick : ThemeSettingsAction
}
