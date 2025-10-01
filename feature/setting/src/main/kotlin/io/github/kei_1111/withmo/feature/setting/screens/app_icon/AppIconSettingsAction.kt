package io.github.kei_1111.withmo.feature.setting.screens.app_icon

import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape

sealed interface AppIconSettingsAction : Action {
    data class OnAppIconShapeRadioButtonClick(val appIconShape: AppIconShape) : AppIconSettingsAction
    data class OnRoundedCornerPercentSliderChange(val roundedCornerPercent: Float) : AppIconSettingsAction
    data object OnSaveButtonClick : AppIconSettingsAction
    data object OnBackButtonClick : AppIconSettingsAction
}
