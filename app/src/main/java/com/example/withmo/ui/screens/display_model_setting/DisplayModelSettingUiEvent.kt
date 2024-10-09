package com.example.withmo.ui.screens.display_model_setting

import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.base.UiEvent

sealed interface DisplayModelSettingUiEvent : UiEvent {
    data class SelectModelFile(val modelFile: ModelFile) : DisplayModelSettingUiEvent
    data object NavigateToSettingsScreen : DisplayModelSettingUiEvent
}
