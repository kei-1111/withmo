package io.github.kei_1111.withmo.feature.setting.side_button

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideButtonSettingsViewModel @Inject constructor(
    private val getSideButtonSettingsUseCase: GetSideButtonSettingsUseCase,
    private val saveSideButtonSettingsUseCase: SaveSideButtonSettingsUseCase,
) : BaseViewModel<SideButtonSettingsState, SideButtonSettingsAction, SideButtonSettingsEffect>() {

    override fun createInitialState(): SideButtonSettingsState = SideButtonSettingsState()

    init {
        observeSideButtonSettings()
    }

    private fun observeSideButtonSettings() {
        viewModelScope.launch {
            getSideButtonSettingsUseCase().collect { sideButtonSettings ->
                updateState {
                    copy(
                        sideButtonSettings = sideButtonSettings,
                        initialSideButtonSettings = sideButtonSettings,
                    )
                }
            }
        }
    }

    private fun saveSideButtonSettings() {
        updateState { copy(isSaveButtonEnabled = false) }
        viewModelScope.launch {
            try {
                saveSideButtonSettingsUseCase(state.value.sideButtonSettings)
                sendEffect(SideButtonSettingsEffect.ShowToast("保存しました"))
                sendEffect(SideButtonSettingsEffect.NavigateBack)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save side button settings", e)
                sendEffect(SideButtonSettingsEffect.ShowToast("保存に失敗しました"))
            }
        }
    }

    override fun onAction(action: SideButtonSettingsAction) {
        when (action) {
            is SideButtonSettingsAction.OnIsShowScaleSliderButtonShownSwitchChange -> {
                updateState {
                    val updatedSideButtonSettings = sideButtonSettings.copy(isShowScaleSliderButtonShown = action.isShowScaleSliderButtonShown)
                    copy(
                        sideButtonSettings = updatedSideButtonSettings,
                        isSaveButtonEnabled = updatedSideButtonSettings != initialSideButtonSettings,
                    )
                }
            }

            is SideButtonSettingsAction.OnIsOpenDocumentButtonShownSwitchChange -> {
                updateState {
                    val updatedSideButtonSettings = sideButtonSettings.copy(isOpenDocumentButtonShown = action.isOpenDocumentButtonShown)
                    copy(
                        sideButtonSettings = updatedSideButtonSettings,
                        isSaveButtonEnabled = updatedSideButtonSettings != initialSideButtonSettings,
                    )
                }
            }

            is SideButtonSettingsAction.OnIsSetDefaultModelButtonShownSwitchChange -> {
                updateState {
                    val updatedSideButtonSettings = sideButtonSettings.copy(isSetDefaultModelButtonShown = action.isSetDefaultModelButtonShown)
                    copy(
                        sideButtonSettings = updatedSideButtonSettings,
                        isSaveButtonEnabled = updatedSideButtonSettings != initialSideButtonSettings,
                    )
                }
            }

            is SideButtonSettingsAction.OnIsNavigateSettingsButtonShownSwitchChange -> {
                updateState {
                    val updatedSideButtonSettings = sideButtonSettings.copy(isNavigateSettingsButtonShown = action.isNavigateSettingsButtonShown)
                    copy(
                        sideButtonSettings = updatedSideButtonSettings,
                        isSaveButtonEnabled = updatedSideButtonSettings != initialSideButtonSettings,
                    )
                }
            }

            is SideButtonSettingsAction.OnSaveButtonClick -> saveSideButtonSettings()

            is SideButtonSettingsAction.OnBackButtonClick -> sendEffect(SideButtonSettingsEffect.NavigateBack)
        }
    }

    private companion object {
        const val TAG = "SideButtonSettingsViewModel"
    }
}
