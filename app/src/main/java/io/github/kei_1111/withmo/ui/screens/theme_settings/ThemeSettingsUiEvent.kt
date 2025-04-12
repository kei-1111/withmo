package io.github.kei_1111.withmo.ui.screens.theme_settings

import io.github.kei_1111.withmo.domain.model.user_settings.ThemeType
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface ThemeSettingsUiEvent : UiEvent {
    data class OnThemeTypeRadioButtonClick(val themeType: ThemeType) : ThemeSettingsUiEvent
    data object OnSaveButtonClick : ThemeSettingsUiEvent
    data object OnBackButtonClick : ThemeSettingsUiEvent
}
