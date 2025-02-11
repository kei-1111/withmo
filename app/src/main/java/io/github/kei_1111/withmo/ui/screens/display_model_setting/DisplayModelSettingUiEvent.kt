package io.github.kei_1111.withmo.ui.screens.display_model_setting

import io.github.kei_1111.withmo.domain.model.ModelFile
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface DisplayModelSettingUiEvent : UiEvent {
    data class SelectModelFile(val modelFile: ModelFile) : DisplayModelSettingUiEvent
    data object NavigateToSettingsScreen : DisplayModelSettingUiEvent
}
