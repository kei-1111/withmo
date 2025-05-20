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
) : BaseViewModel<SideButtonSettingsUiState, SideButtonSettingsAction>() {

    override fun createInitialState(): SideButtonSettingsUiState = SideButtonSettingsUiState()

    init {
        observeSideButtonSettings()
    }

    private fun observeSideButtonSettings() {
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

    fun changeIsShowScaleSliderButtonShown(isScaleSliderButtonShown: Boolean) {
        _uiState.update {
            it.copy(
                sideButtonSettings = it.sideButtonSettings.copy(
                    isShowScaleSliderButtonShown = isScaleSliderButtonShown,
                ),
                isSaveButtonEnabled =
                isScaleSliderButtonShown != it.initialSideButtonSettings.isShowScaleSliderButtonShown,
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

    fun changeIsNavigateSettingsButtonShown(isNavigateSettingsButtonShown: Boolean) {
        _uiState.update {
            it.copy(
                sideButtonSettings = it.sideButtonSettings.copy(
                    isNavigateSettingsButtonShown = isNavigateSettingsButtonShown,
                ),
                isSaveButtonEnabled =
                isNavigateSettingsButtonShown != it.initialSideButtonSettings.isNavigateSettingsButtonShown,
            )
        }
    }

    fun saveSideButtonSettings(
        onSaveSuccess: () -> Unit,
        onSaveFailure: () -> Unit,
    ) {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                saveSideButtonSettingsUseCase(_uiState.value.sideButtonSettings)
                onSaveSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save side button settings", e)
                onSaveFailure()
            }
        }
    }

    private companion object {
        const val TAG = "SideButtonSettingsViewModel"
    }
}
