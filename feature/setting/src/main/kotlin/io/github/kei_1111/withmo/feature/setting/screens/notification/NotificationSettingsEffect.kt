package io.github.kei_1111.withmo.feature.setting.screens.notification

import io.github.kei_1111.withmo.core.featurebase.Effect

internal sealed interface NotificationSettingsEffect : Effect {
    data object NavigateBack : NotificationSettingsEffect
    data class ShowToast(val message: String) : NotificationSettingsEffect
}
