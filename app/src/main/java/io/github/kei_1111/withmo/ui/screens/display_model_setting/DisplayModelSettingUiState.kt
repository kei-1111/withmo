package io.github.kei_1111.withmo.ui.screens.display_model_setting

import io.github.kei_1111.withmo.domain.model.ModelFile
import io.github.kei_1111.withmo.domain.model.user_settings.DisplayModelSetting
import io.github.kei_1111.withmo.ui.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DisplayModelSettingUiState(
    val modelFileList: ImmutableList<ModelFile> = persistentListOf(),
    val displayModelSetting: DisplayModelSetting = DisplayModelSetting(),
    val initialDisplayModelSetting: DisplayModelSetting = DisplayModelSetting(),
    val isSaveButtonEnabled: Boolean = false,
) : UiState
