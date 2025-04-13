package io.github.kei_1111.withmo.ui.screens.settings

import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface SettingsUiEvent : UiEvent {
    data object OnNavigateHomeAppSettingButtonClick : SettingsUiEvent
    data object OnNavigateClockSettingsButtonClick : SettingsUiEvent
    data object OnNavigateAppIconSettingsButtonClick : SettingsUiEvent
    data object OnNavigateFavoriteAppSettingsButtonClick : SettingsUiEvent
    data object OnNavigateSideButtonSettingsButtonClick : SettingsUiEvent
    data object OnNavigateSortSettingsButtonClick : SettingsUiEvent
    data object OnNavigateNotificationSettingsButtonClick : SettingsUiEvent
    data object OnNavigateThemeSettingsButtonClick : SettingsUiEvent
    data object OnBackButtonClick : SettingsUiEvent
}
