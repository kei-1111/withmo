package io.github.kei_1111.withmo.feature.setting.app_icon

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppIconSettingsViewModel @Inject constructor(
    private val getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
    private val saveAppIconSettingsUseCase: SaveAppIconSettingsUseCase,
) : BaseViewModel<AppIconSettingsState, AppIconSettingsAction, AppIconSettingsEffect>() {

    override fun createInitialState(): AppIconSettingsState = AppIconSettingsState()

    init {
        observeAppIconSettings()
    }

    private fun observeAppIconSettings() {
        viewModelScope.launch {
            getAppIconSettingsUseCase().collect { appIconSettings ->
                updateState {
                    copy(
                        appIconSettings = appIconSettings,
                        initialAppIconSettings = appIconSettings,
                    )
                }
            }
        }
    }

    private fun saveAppIconSettings() {
        updateState { copy(isSaveButtonEnabled = false) }
        viewModelScope.launch {
            try {
                saveAppIconSettingsUseCase(state.value.appIconSettings)
                sendEffect(AppIconSettingsEffect.ShowToast("保存しました"))
                sendEffect(AppIconSettingsEffect.NavigateBack)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save app icon settings", e)
                sendEffect(AppIconSettingsEffect.ShowToast("保存に失敗しました"))
            }
        }
    }

    override fun onAction(action: AppIconSettingsAction) {
        when (action) {
            is AppIconSettingsAction.OnAppIconShapeRadioButtonClick -> {
                updateState {
                    val updatedAppIconSettings = appIconSettings.copy(appIconShape = action.appIconShape)
                    copy(
                        appIconSettings = updatedAppIconSettings,
                        isSaveButtonEnabled = updatedAppIconSettings != initialAppIconSettings,
                    )
                }
            }

            is AppIconSettingsAction.OnRoundedCornerPercentSliderChange -> {
                updateState {
                    val updatedAppIconSettings = appIconSettings.copy(roundedCornerPercent = action.roundedCornerPercent)
                    copy(
                        appIconSettings = updatedAppIconSettings,
                        isSaveButtonEnabled = updatedAppIconSettings != initialAppIconSettings,
                    )
                }
            }

            is AppIconSettingsAction.OnSaveButtonClick -> saveAppIconSettings()

            is AppIconSettingsAction.OnBackButtonClick -> sendEffect(AppIconSettingsEffect.NavigateBack)
        }
    }

    private companion object {
        const val TAG = "AppIconSettingsViewModel"
    }
}
