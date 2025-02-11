package io.github.kei_1111.withmo.ui.screens.display_model_setting

import io.github.kei_1111.withmo.domain.model.ModelFile
import io.github.kei_1111.withmo.ui.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DisplayModelSettingUiState(
    val modelFileList: ImmutableList<ModelFile> = persistentListOf(),
    val selectedModelFile: ModelFile? = null,
) : UiState
