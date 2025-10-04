package io.github.kei_1111.withmo.feature.setting.screens.side_button

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SideButtonSettingsViewModel @Inject constructor(
    getSideButtonSettingsUseCase: GetSideButtonSettingsUseCase,
    private val saveSideButtonSettingsUseCase: SaveSideButtonSettingsUseCase,
) : StatefulBaseViewModel<SideButtonSettingsViewModelState, SideButtonSettingsState, SideButtonSettingsAction, SideButtonSettingsEffect>() {

    override fun createInitialViewModelState() = SideButtonSettingsViewModelState()
    override fun createInitialState() = SideButtonSettingsState.Idle

    private val sideButtonSettingsDataStream = getSideButtonSettingsUseCase()

    init {
        viewModelScope.launch {
            updateViewModelState { copy(statusType = SideButtonSettingsViewModelState.StatusType.LOADING) }
            sideButtonSettingsDataStream.collect { result ->
                result
                    .onSuccess { sideButtonSettings ->
                        updateViewModelState {
                            copy(
                                statusType = SideButtonSettingsViewModelState.StatusType.STABLE,
                                sideButtonSettings = sideButtonSettings,
                                initialSideButtonSettings = sideButtonSettings,
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = SideButtonSettingsViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
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
                        saveSideButtonSettingsUseCase(_viewModelState.value.sideButtonSettings)
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
