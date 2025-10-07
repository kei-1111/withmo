package io.github.kei_1111.withmo.feature.setting.screen.app_icon

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AppIconSettingsViewModel @Inject constructor(
    getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
    private val saveAppIconSettingsUseCase: SaveAppIconSettingsUseCase,
) : StatefulBaseViewModel<AppIconSettingsViewModelState, AppIconSettingsState, AppIconSettingsAction, AppIconSettingsEffect>() {

    override fun createInitialViewModelState() = AppIconSettingsViewModelState()
    override fun createInitialState() = AppIconSettingsState.Idle

    private val appIconSettingsDataStream = getAppIconSettingsUseCase()

    init {
        viewModelScope.launch {
            updateViewModelState { copy(statusType = AppIconSettingsViewModelState.StatusType.LOADING) }
            appIconSettingsDataStream.collect { result ->
                result
                    .onSuccess { appIconSettings ->
                        updateViewModelState {
                            copy(
                                statusType = AppIconSettingsViewModelState.StatusType.STABLE,
                                appIconSettings = appIconSettings,
                                initialAppIconSettings = appIconSettings,
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = AppIconSettingsViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
                    }
            }
        }
    }

    override fun onAction(action: AppIconSettingsAction) {
        when (action) {
            is AppIconSettingsAction.OnAppIconShapeRadioButtonClick -> {
                updateViewModelState {
                    val updatedAppIconSettings = appIconSettings.copy(appIconShape = action.appIconShape)
                    copy(appIconSettings = updatedAppIconSettings)
                }
            }

            is AppIconSettingsAction.OnRoundedCornerPercentSliderChange -> {
                updateViewModelState {
                    val updatedAppIconSettings = appIconSettings.copy(roundedCornerPercent = action.roundedCornerPercent)
                    copy(appIconSettings = updatedAppIconSettings)
                }
            }

            is AppIconSettingsAction.OnSaveButtonClick -> {
                viewModelScope.launch {
                    try {
                        saveAppIconSettingsUseCase(_viewModelState.value.appIconSettings)
                        sendEffect(AppIconSettingsEffect.ShowToast("保存しました"))
                        sendEffect(AppIconSettingsEffect.NavigateBack)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to save app icon settings", e)
                        sendEffect(AppIconSettingsEffect.ShowToast("保存に失敗しました"))
                    }
                }
            }

            is AppIconSettingsAction.OnBackButtonClick -> {
                sendEffect(AppIconSettingsEffect.NavigateBack)
            }
        }
    }

    private companion object {
        const val TAG = "AppIconSettingsViewModel"
    }
}
