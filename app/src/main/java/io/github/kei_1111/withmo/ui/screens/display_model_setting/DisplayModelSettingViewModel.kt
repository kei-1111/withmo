package io.github.kei_1111.withmo.ui.screens.display_model_setting

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.model.ModelFile
import io.github.kei_1111.withmo.domain.model.user_settings.DisplayModelSetting
import io.github.kei_1111.withmo.domain.usecase.user_settings.display_model.GetDisplayModelSettingUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.display_model.SaveDisplayModelSettingUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import io.github.kei_1111.withmo.ui.screens.clock_settings.ClockSettingsUiEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayModelSettingViewModel @Inject constructor(
    private val getDisplayModelSettingUseCase: GetDisplayModelSettingUseCase,
    private val saveDisplayModelSettingUseCase: SaveDisplayModelSettingUseCase,
) :
    BaseViewModel<DisplayModelSettingUiState, DisplayModelSettingUiEvent>() {

    override fun createInitialState(): DisplayModelSettingUiState = DisplayModelSettingUiState()

    init {
        getDisplayModelSetting()
    }

    private fun getDisplayModelSetting() {
        viewModelScope.launch {
            getDisplayModelSettingUseCase().collect { displayModelSetting ->
                _uiState.update {
                    it.copy(
                        displayModelSetting = displayModelSetting,
                        initialDisplayModelSetting = displayModelSetting,
                    )
                }
            }
        }
    }

    fun selectModelFile(modelFile: ModelFile) {
        _uiState.update {
            it.copy(
                displayModelSetting = it.displayModelSetting.copy(
                    modelFile = modelFile,
                ),
                isSaveButtonEnabled = modelFile.filePath != it.initialDisplayModelSetting.modelFile?.filePath,
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

    fun saveDisplayModelSetting() {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                saveDisplayModelSettingUseCase(_uiState.value.displayModelSetting)
                _uiEvent.emit(DisplayModelSettingUiEvent.SaveSuccess)
            } catch (e: Exception) {
                Log.e("DisplayModelSettingViewModel", "Failed to save display model settings", e)
                _uiEvent.emit(DisplayModelSettingUiEvent.SaveFailure)
            }
        }
    }
}
