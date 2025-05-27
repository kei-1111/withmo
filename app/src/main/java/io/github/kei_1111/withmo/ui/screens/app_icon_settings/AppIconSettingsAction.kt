package io.github.kei_1111.withmo.ui.screens.app_icon_settings

import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.domain.model.user_settings.AppIconShape

sealed interface AppIconSettingsAction : Action {
    data class OnAppIconSizeSliderChange(val appIconSize: Float) : AppIconSettingsAction
    data class OnAppIconShapeRadioButtonClick(val appIconShape: AppIconShape) : AppIconSettingsAction
    data class OnRoundedCornerPercentSliderChange(val roundedCornerPercent: Float) : AppIconSettingsAction
    data class OnIsAppNameShownSwitchChange(val isAppNameShown: Boolean) : AppIconSettingsAction
    data class OnIsFavoriteAppBackgroundShownSwitchChange(val isFavoriteAppBackgroundShown: Boolean) : AppIconSettingsAction
    data object OnSaveButtonClick : AppIconSettingsAction
    data object OnBackButtonClick : AppIconSettingsAction
}
