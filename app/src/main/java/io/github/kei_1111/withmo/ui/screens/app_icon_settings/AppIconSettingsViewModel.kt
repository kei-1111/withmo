package io.github.kei_1111.withmo.ui.screens.app_icon_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.SaveAppIconSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
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
            is AppIconSettingsAction.OnAppIconSizeSliderChange -> {
                updateState {
                    copy(
                        appIconSettings = appIconSettings.copy(appIconSize = action.appIconSize),
                        isSaveButtonEnabled = action.appIconSize != initialAppIconSettings.appIconSize,
                    )
                }
            }

            is AppIconSettingsAction.OnAppIconShapeRadioButtonClick -> {
                updateState {
                    copy(
                        appIconSettings = appIconSettings.copy(appIconShape = action.appIconShape),
                        isSaveButtonEnabled = action.appIconShape != initialAppIconSettings.appIconShape,
                    )
                }
            }

            is AppIconSettingsAction.OnRoundedCornerPercentSliderChange -> {
                updateState {
                    copy(
                        appIconSettings = appIconSettings.copy(roundedCornerPercent = action.roundedCornerPercent),
                        isSaveButtonEnabled = action.roundedCornerPercent != initialAppIconSettings.roundedCornerPercent,
                    )
                }
            }

            is AppIconSettingsAction.OnIsAppNameShownSwitchChange -> {
                updateState {
                    copy(
                        appIconSettings = appIconSettings.copy(isAppNameShown = action.isAppNameShown),
                        isSaveButtonEnabled = action.isAppNameShown != initialAppIconSettings.isAppNameShown,
                    )
                }
            }

            is AppIconSettingsAction.OnIsFavoriteAppBackgroundShownSwitchChange -> {
                updateState {
                    copy(
                        appIconSettings = appIconSettings.copy(isFavoriteAppBackgroundShown = action.isFavoriteAppBackgroundShown),
                        isSaveButtonEnabled = action.isFavoriteAppBackgroundShown != initialAppIconSettings.isFavoriteAppBackgroundShown,
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
