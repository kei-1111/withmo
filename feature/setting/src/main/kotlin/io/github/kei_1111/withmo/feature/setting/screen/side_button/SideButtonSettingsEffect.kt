package io.github.kei_1111.withmo.feature.setting.screen.side_button

import io.github.kei_1111.withmo.core.featurebase.Effect

internal sealed interface SideButtonSettingsEffect : Effect {
    data object NavigateBack : SideButtonSettingsEffect
    data class ShowToast(val message: String) : SideButtonSettingsEffect
}
