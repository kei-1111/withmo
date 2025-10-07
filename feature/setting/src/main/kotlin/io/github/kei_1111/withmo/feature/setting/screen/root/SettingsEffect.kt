package io.github.kei_1111.withmo.feature.setting.screen.root

import android.content.Intent
import io.github.kei_1111.withmo.core.featurebase.Effect

internal sealed interface SettingsEffect : Effect {
    data class OpenHomeAppSettings(val intent: Intent) : SettingsEffect
    data object NavigateClockSettings : SettingsEffect
    data object NavigateAppIconSettings : SettingsEffect
    data object NavigateFavoriteAppSettings : SettingsEffect
    data object NavigateSideButtonSettings : SettingsEffect
    data object NavigateSortSettings : SettingsEffect
    data object NavigateNotificationSettings : SettingsEffect
    data class OpenWallpaperSettings(val intent: Intent) : SettingsEffect
    data object NavigateThemeSettings : SettingsEffect
    data object NavigateBack : SettingsEffect
    data class ShowToast(val message: String) : SettingsEffect
    data class RequestNotificationListenerPermission(val intent: Intent) : SettingsEffect
}
