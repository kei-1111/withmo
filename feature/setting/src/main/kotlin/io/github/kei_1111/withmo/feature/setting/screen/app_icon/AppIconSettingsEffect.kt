package io.github.kei_1111.withmo.feature.setting.screen.app_icon

import io.github.kei_1111.withmo.core.featurebase.Effect

internal sealed interface AppIconSettingsEffect : Effect {
    data object NavigateBack : AppIconSettingsEffect
    data class ShowToast(val message: String) : AppIconSettingsEffect
}
