package io.github.kei_1111.withmo.feature.setting.screen.root

import io.github.kei_1111.withmo.core.featurebase.Action

internal sealed interface SettingsAction : Action {
    data class OnSettingsScreenLifecycleChanged(val isDefaultHomeApp: Boolean) : SettingsAction
    data object OnNavigateHomeAppSettingButtonClick : SettingsAction
    data object OnNavigateClockSettingsButtonClick : SettingsAction
    data object OnNavigateAppIconSettingsButtonClick : SettingsAction
    data object OnNavigateFavoriteAppSettingsButtonClick : SettingsAction
    data object OnNavigateSideButtonSettingsButtonClick : SettingsAction
    data object OnNavigateSortSettingsButtonClick : SettingsAction
    data object OnNavigateNotificationSettingsButtonClick : SettingsAction
    data object OnNavigateWallpaperSettingsButtonClick : SettingsAction
    data object OnNavigateThemeSettingsButtonClick : SettingsAction
    data object OnBackButtonClick : SettingsAction
    data object OnNotificationPermissionDialogConfirm : SettingsAction
    data object OnNotificationPermissionDialogDismiss : SettingsAction
    data object OnNotificationListenerPermissionResult : SettingsAction
}
