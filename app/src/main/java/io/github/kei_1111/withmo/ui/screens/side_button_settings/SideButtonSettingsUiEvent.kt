package io.github.kei_1111.withmo.ui.screens.side_button_settings

import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface SideButtonSettingsUiEvent : UiEvent {
    data class OnIsShowScaleSliderButtonShownSwitchChange(val isShowScaleSliderButtonShown: Boolean) : SideButtonSettingsUiEvent
    data class OnIsOpenDocumentButtonShownSwitchChange(val isOpenDocumentButtonShown: Boolean) : SideButtonSettingsUiEvent
    data class OnIsSetDefaultModelButtonShownSwitchChange(val isSetDefaultModelButtonShown: Boolean) : SideButtonSettingsUiEvent
    data object OnSaveButtonClick : SideButtonSettingsUiEvent
    data object OnBackButtonClick : SideButtonSettingsUiEvent
}
