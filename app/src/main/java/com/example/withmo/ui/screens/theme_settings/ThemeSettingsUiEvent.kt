package com.example.withmo.ui.screens.theme_settings

import com.example.withmo.domain.model.user_settings.ThemeType
import com.example.withmo.ui.base.UiEvent

sealed interface ThemeSettingsUiEvent : UiEvent {
    data class ChangeThemeType(val themeType: ThemeType) : ThemeSettingsUiEvent
    data object Save : ThemeSettingsUiEvent
    data object SaveSuccess : ThemeSettingsUiEvent
    data object SaveFailure : ThemeSettingsUiEvent
    data object NavigateToSettingsScreen : ThemeSettingsUiEvent
}
