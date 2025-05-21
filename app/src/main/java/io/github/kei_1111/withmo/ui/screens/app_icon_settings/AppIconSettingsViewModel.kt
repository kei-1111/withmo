package io.github.kei_1111.withmo.ui.screens.app_icon_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.SaveAppIconSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.update
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
                _state.update {
                    it.copy(
                        appIconSettings = appIconSettings,
                        initialAppIconSettings = appIconSettings,
                    )
                }
            }
        }
    }

    private fun changeAppIconSize(appIconSize: Float) {
        _state.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    appIconSize = appIconSize,
                ),
                isSaveButtonEnabled = appIconSize != it.initialAppIconSettings.appIconSize,
            )
        }
    }

    private fun changeAppIconShape(appIconShape: AppIconShape) {
        _state.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    appIconShape = appIconShape,
                ),
                isSaveButtonEnabled = appIconShape != it.initialAppIconSettings.appIconShape,
            )
        }
    }

    private fun changeRoundedCornerPercent(roundedCornerPercent: Float) {
        _state.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    roundedCornerPercent = roundedCornerPercent,
                ),
                isSaveButtonEnabled = roundedCornerPercent != it.initialAppIconSettings.roundedCornerPercent,
            )
        }
    }

    private fun changeIsAppNameShown(isAppNameShown: Boolean) {
        _state.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    isAppNameShown = isAppNameShown,
                ),
                isSaveButtonEnabled = isAppNameShown != it.initialAppIconSettings.isAppNameShown,
            )
        }
    }

    private fun changeIsFavoriteAppBackgroundShown(isFavoriteAppBackgroundShown: Boolean) {
        _state.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    isFavoriteAppBackgroundShown = isFavoriteAppBackgroundShown,
                ),
                isSaveButtonEnabled = isFavoriteAppBackgroundShown != it.initialAppIconSettings.isFavoriteAppBackgroundShown,
            )
        }
    }

    private fun saveAppIconSettings() {
        _state.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
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
            is AppIconSettingsAction.OnAppIconSizeSliderChange -> changeAppIconSize(action.appIconSize)

            is AppIconSettingsAction.OnAppIconShapeRadioButtonClick -> changeAppIconShape(action.appIconShape)

            is AppIconSettingsAction.OnRoundedCornerPercentSliderChange -> changeRoundedCornerPercent(action.roundedCornerPercent)

            is AppIconSettingsAction.OnIsAppNameShownSwitchChange -> changeIsAppNameShown(action.isAppNameShown)

            is AppIconSettingsAction.OnIsFavoriteAppBackgroundShownSwitchChange ->
                changeIsFavoriteAppBackgroundShown(action.isFavoriteAppBackgroundShown)

            is AppIconSettingsAction.OnSaveButtonClick -> saveAppIconSettings()

            is AppIconSettingsAction.OnBackButtonClick -> sendEffect(AppIconSettingsEffect.NavigateBack)
        }
    }

    private companion object {
        const val TAG = "AppIconSettingsViewModel"
    }
}
