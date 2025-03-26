package io.github.kei_1111.withmo.ui.screens.display_model_setting

import io.github.kei_1111.withmo.domain.model.ModelFile
import io.github.kei_1111.withmo.ui.base.UiEvent
import io.github.kei_1111.withmo.ui.screens.clock_settings.ClockSettingsUiEvent

sealed interface DisplayModelSettingUiEvent : UiEvent {
    data class SelectModelFile(val modelFile: ModelFile) : DisplayModelSettingUiEvent
    data object NavigateToSettingsScreen : DisplayModelSettingUiEvent
    data object Save : DisplayModelSettingUiEvent
    data object SaveSuccess : DisplayModelSettingUiEvent
    data object SaveFailure : DisplayModelSettingUiEvent
}
