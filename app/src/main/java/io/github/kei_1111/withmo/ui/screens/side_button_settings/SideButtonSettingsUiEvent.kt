package io.github.kei_1111.withmo.ui.screens.side_button_settings

import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface SideButtonSettingsUiEvent : UiEvent {
    data class ChangeIsShowScaleSliderButtonShown(
        val isShowScaleSliderButtonShown: Boolean,
    ) : SideButtonSettingsUiEvent
    data class ChangeIsOpenDocumentButtonShown(
        val isOpenDocumentButtonShown: Boolean,
    ) : SideButtonSettingsUiEvent
    data class ChangeIsSetDefaultModelButtonShown(
        val isSetDefaultModelButtonShown: Boolean,
    ) : SideButtonSettingsUiEvent
    data object Save : SideButtonSettingsUiEvent
    data object SaveSuccess : SideButtonSettingsUiEvent
    data object SaveFailure : SideButtonSettingsUiEvent
    data object NavigateToSettingsScreen : SideButtonSettingsUiEvent
}
