package com.example.withmo.ui.screens.display_model_setting

import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DisplayModelSettingViewModel @Inject constructor() :
    BaseViewModel<DisplayModelSettingUiState, DisplayModelSettingUiEvent>() {

    override fun createInitialState(): DisplayModelSettingUiState = DisplayModelSettingUiState()

    fun selectModelFile(modelFile: ModelFile) {
        _uiState.update {
            it.copy(
                selectedModelFile = modelFile,
            )
        }
    }

    fun getModelFileList(modelFileList: ImmutableList<ModelFile>) {
        _uiState.update {
            it.copy(
                modelFileList = modelFileList,
            )
        }
    }
}
