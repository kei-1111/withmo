package io.github.kei_1111.withmo.ui.screens.settings

import io.github.kei_1111.withmo.core.featurebase.Action

sealed interface SettingsAction : Action {
    data class OnSettingsScreenLifecycleChanged(val isDefaultHomeApp: Boolean) : SettingsAction
    data object OnNavigateHomeAppSettingButtonClick : SettingsAction
    data object OnNavigateClockSettingsButtonClick : SettingsAction
    data object OnNavigateAppIconSettingsButtonClick : SettingsAction
    data object OnNavigateFavoriteAppSettingsButtonClick : SettingsAction
    data object OnNavigateSideButtonSettingsButtonClick : SettingsAction
    data object OnNavigateSortSettingsButtonClick : SettingsAction
    data object OnNavigateNotificationSettingsButtonClick : SettingsAction
    data object OnNavigateThemeSettingsButtonClick : SettingsAction
    data object OnBackButtonClick : SettingsAction
}
