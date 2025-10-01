package io.github.kei_1111.withmo.feature.setting.screens.side_button

import io.github.kei_1111.withmo.core.featurebase.Action

sealed interface SideButtonSettingsAction : Action {
    data class OnIsShowScaleSliderButtonShownSwitchChange(val isShowScaleSliderButtonShown: Boolean) : SideButtonSettingsAction
    data class OnIsOpenDocumentButtonShownSwitchChange(val isOpenDocumentButtonShown: Boolean) : SideButtonSettingsAction
    data class OnIsSetDefaultModelButtonShownSwitchChange(val isSetDefaultModelButtonShown: Boolean) : SideButtonSettingsAction
    data class OnIsNavigateSettingsButtonShownSwitchChange(val isNavigateSettingsButtonShown: Boolean) : SideButtonSettingsAction
    data object OnSaveButtonClick : SideButtonSettingsAction
    data object OnBackButtonClick : SideButtonSettingsAction
}
