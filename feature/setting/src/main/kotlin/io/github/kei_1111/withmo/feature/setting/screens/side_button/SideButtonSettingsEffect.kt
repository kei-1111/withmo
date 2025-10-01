package io.github.kei_1111.withmo.feature.setting.screens.side_button

import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface SideButtonSettingsEffect : Effect {
    data object NavigateBack : SideButtonSettingsEffect
    data class ShowToast(val message: String) : SideButtonSettingsEffect
}
