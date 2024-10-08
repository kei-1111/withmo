package com.example.withmo.ui.screens.side_button

sealed interface SideButtonSettingsUiEvent {
    data class ChangeIsScaleSliderButtonShown(
        val isScaleSliderButtonShown: Boolean,
    ) : SideButtonSettingsUiEvent
    data class ChangeIsSortButtonShown(
        val isSortButtonShown: Boolean,
    ) : SideButtonSettingsUiEvent
    data object Save : SideButtonSettingsUiEvent
    data object SaveSuccess : SideButtonSettingsUiEvent
    data object SaveFailure : SideButtonSettingsUiEvent
    data object NavigateToSettingsScreen : SideButtonSettingsUiEvent
}
