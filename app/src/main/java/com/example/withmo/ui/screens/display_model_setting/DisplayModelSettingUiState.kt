package com.example.withmo.ui.screens.display_model_setting

import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DisplayModelSettingUiState(
    val modelFileList: ImmutableList<ModelFile> = persistentListOf(),
    val selectedModelFile: ModelFile? = null,
) : UiState
