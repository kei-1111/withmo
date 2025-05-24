package io.github.kei_1111.withmo.ui.screens.side_button_settings

import io.github.kei_1111.withmo.ui.base.Effect

sealed interface SideButtonSettingsEffect : Effect {
    data object NavigateBack : SideButtonSettingsEffect
    data class ShowToast(val message: String) : SideButtonSettingsEffect
}
