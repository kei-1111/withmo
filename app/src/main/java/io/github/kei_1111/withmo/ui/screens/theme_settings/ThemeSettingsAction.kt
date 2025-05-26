package io.github.kei_1111.withmo.ui.screens.theme_settings

import io.github.kei_1111.withmo.domain.model.user_settings.ThemeType
import io.github.kei_1111.withmo.ui.base.Action

sealed interface ThemeSettingsAction : Action {
    data class OnThemeTypeRadioButtonClick(val themeType: ThemeType) : ThemeSettingsAction
    data object OnSaveButtonClick : ThemeSettingsAction
    data object OnBackButtonClick : ThemeSettingsAction
}
