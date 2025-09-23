package io.github.kei_1111.withmo.feature.setting.side_button

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideButtonSettingsViewModel @Inject constructor(
    private val getSideButtonSettingsUseCase: GetSideButtonSettingsUseCase,
    private val saveSideButtonSettingsUseCase: SaveSideButtonSettingsUseCase,
) : StatefulBaseViewModel<SideButtonSettingsViewModelState, SideButtonSettingsState, SideButtonSettingsAction, SideButtonSettingsEffect>() {

    override fun createInitialViewModelState() = SideButtonSettingsViewModelState()
    override fun createInitialState() = SideButtonSettingsState()

    init {
        observeSideButtonSettings()
    }

    private fun observeSideButtonSettings() {
        viewModelScope.launch {
            getSideButtonSettingsUseCase().collect { sideButtonSettings ->
                updateViewModelState {
                    copy(
                        sideButtonSettings = sideButtonSettings,
                        initialSideButtonSettings = sideButtonSettings,
                    )
                }
            }
        }
    }

    override fun onAction(action: SideButtonSettingsAction) {
        when (action) {
            is SideButtonSettingsAction.OnIsShowScaleSliderButtonShownSwitchChange -> {
                updateViewModelState {
                    val updatedSideButtonSettings = sideButtonSettings.copy(isShowScaleSliderButtonShown = action.isShowScaleSliderButtonShown)
                    copy(sideButtonSettings = updatedSideButtonSettings)
                }
            }

            is SideButtonSettingsAction.OnIsOpenDocumentButtonShownSwitchChange -> {
                updateViewModelState {
                    val updatedSideButtonSettings = sideButtonSettings.copy(isOpenDocumentButtonShown = action.isOpenDocumentButtonShown)
                    copy(sideButtonSettings = updatedSideButtonSettings)
                }
            }

            is SideButtonSettingsAction.OnIsSetDefaultModelButtonShownSwitchChange -> {
                updateViewModelState {
                    val updatedSideButtonSettings = sideButtonSettings.copy(isSetDefaultModelButtonShown = action.isSetDefaultModelButtonShown)
                    copy(sideButtonSettings = updatedSideButtonSettings)
                }
            }

            is SideButtonSettingsAction.OnIsNavigateSettingsButtonShownSwitchChange -> {
                updateViewModelState {
                    val updatedSideButtonSettings = sideButtonSettings.copy(isNavigateSettingsButtonShown = action.isNavigateSettingsButtonShown)
                    copy(sideButtonSettings = updatedSideButtonSettings)
                }
            }

            is SideButtonSettingsAction.OnSaveButtonClick -> {
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

            is SideButtonSettingsAction.OnBackButtonClick -> {
                sendEffect(SideButtonSettingsEffect.NavigateBack)
            }
        }
    }

    private companion object {
        const val TAG = "SideButtonSettingsViewModel"
    }
}
