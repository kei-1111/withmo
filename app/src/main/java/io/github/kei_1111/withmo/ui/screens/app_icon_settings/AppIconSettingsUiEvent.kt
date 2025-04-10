package io.github.kei_1111.withmo.ui.screens.app_icon_settings

import io.github.kei_1111.withmo.domain.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface AppIconSettingsUiEvent : UiEvent {
    data class OnAppIconSizeSliderChange(val appIconSize: Float) : AppIconSettingsUiEvent
    data class OnAppIconShapeRadioButtonClick(val appIconShape: AppIconShape) : AppIconSettingsUiEvent
    data class OnRoundedCornerPercentSliderChange(val roundedCornerPercent: Float) : AppIconSettingsUiEvent
    data class OnIsAppNameShownSwitchChange(val isAppNameShown: Boolean) : AppIconSettingsUiEvent
    data object OnSaveButtonClick : AppIconSettingsUiEvent
    data object OnNavigateToSettingsScreenButtonClick : AppIconSettingsUiEvent
    data object OnBackPress : AppIconSettingsUiEvent
}
