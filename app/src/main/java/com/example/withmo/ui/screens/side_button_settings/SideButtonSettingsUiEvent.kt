package com.example.withmo.ui.screens.side_button_settings

import com.example.withmo.ui.base.UiEvent

sealed interface SideButtonSettingsUiEvent : UiEvent {
    data class ChangeIsScaleSliderButtonShown(
        val isScaleSliderButtonShown: Boolean,
    ) : SideButtonSettingsUiEvent
    data object Save : SideButtonSettingsUiEvent
    data object SaveSuccess : SideButtonSettingsUiEvent
    data object SaveFailure : SideButtonSettingsUiEvent
    data object NavigateToSettingsScreen : SideButtonSettingsUiEvent
}
