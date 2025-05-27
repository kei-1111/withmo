package io.github.kei_1111.withmo.ui.screens.settings

import android.content.Intent
import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface SettingsEffect : Effect {
    data class OpenHomeAppSettings(val intent: Intent) : SettingsEffect
    data object NavigateClockSettings : SettingsEffect
    data object NavigateAppIconSettings : SettingsEffect
    data object NavigateFavoriteAppSettings : SettingsEffect
    data object NavigateSideButtonSettings : SettingsEffect
    data object NavigateSortSettings : SettingsEffect
    data object NavigateNotificationSettings : SettingsEffect
    data object NavigateThemeSettings : SettingsEffect
    data object NavigateBack : SettingsEffect
    data class ShowToast(val message: String) : SettingsEffect
}
