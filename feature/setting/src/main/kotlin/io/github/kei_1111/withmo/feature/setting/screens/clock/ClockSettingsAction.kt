package io.github.kei_1111.withmo.feature.setting.screens.clock

import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.user_settings.ClockType

internal sealed interface ClockSettingsAction : Action {
    data class OnIsClockShownSwitchChange(val isClockShown: Boolean) : ClockSettingsAction
    data class OnClockTypeRadioButtonClick(val clockType: ClockType) : ClockSettingsAction
    data object OnSaveButtonClick : ClockSettingsAction
    data object OnBackButtonClick : ClockSettingsAction
}
