package io.github.kei_1111.withmo.ui.screens.side_button_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.usecase.user_settings.side_button.GetSideButtonSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.side_button.SaveSideButtonSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideButtonSettingsViewModel @Inject constructor(
    private val getSideButtonSettingsUseCase: GetSideButtonSettingsUseCase,
    private val saveSideButtonSettingsUseCase: SaveSideButtonSettingsUseCase,
) : BaseViewModel<SideButtonSettingsUiState, SideButtonSettingsUiEvent>() {

    override fun createInitialState(): SideButtonSettingsUiState = SideButtonSettingsUiState()

    init {
        viewModelScope.launch {
            getSideButtonSettingsUseCase().collect { sideButtonSettings ->
                _uiState.update {
                    it.copy(
                        sideButtonSettings = sideButtonSettings,
                        initialSideButtonSettings = sideButtonSettings,
                    )
                }
            }
        }
    }

    fun changeIsScaleSliderButtonShown(isScaleSliderButtonShown: Boolean) {
        _uiState.update {
            it.copy(
                sideButtonSettings = it.sideButtonSettings.copy(
                    isScaleSliderButtonShown = isScaleSliderButtonShown,
                ),
                isSaveButtonEnabled =
                isScaleSliderButtonShown != it.initialSideButtonSettings.isScaleSliderButtonShown,
            )
        }
    }

    fun changeIsOpenDocumentButtonShown(isOpenDocumentButtonShown: Boolean) {
        _uiState.update {
            it.copy(
                sideButtonSettings = it.sideButtonSettings.copy(
                    isOpenDocumentButtonShown = isOpenDocumentButtonShown,
                ),
                isSaveButtonEnabled =
                isOpenDocumentButtonShown != it.initialSideButtonSettings.isOpenDocumentButtonShown,
            )
        }
    }

    fun changeIsSetDefaultModelButtonShown(isSetDefaultModelButtonShown: Boolean) {
        _uiState.update {
            it.copy(
                sideButtonSettings = it.sideButtonSettings.copy(
                    isSetDefaultModelButtonShown = isSetDefaultModelButtonShown,
                ),
                isSaveButtonEnabled =
                isSetDefaultModelButtonShown != it.initialSideButtonSettings.isSetDefaultModelButtonShown,
            )
        }
    }

    fun saveSideButtonSettings() {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                saveSideButtonSettingsUseCase(_uiState.value.sideButtonSettings)
                _uiEvent.emit(SideButtonSettingsUiEvent.SaveSuccess)
            } catch (e: Exception) {
                Log.e("SideButtonSettingsViewModel", "Failed to save side button settings", e)
                _uiEvent.emit(SideButtonSettingsUiEvent.SaveFailure)
            }
        }
    }
}
