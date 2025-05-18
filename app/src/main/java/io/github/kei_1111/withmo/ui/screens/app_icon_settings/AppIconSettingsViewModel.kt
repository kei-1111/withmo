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
) : BaseViewModel<AppIconSettingsUiState, AppIconSettingsUiEvent>() {

    override fun createInitialState(): AppIconSettingsUiState = AppIconSettingsUiState()

    init {
        observeAppIconSettings()
    }

    private fun observeAppIconSettings() {
        viewModelScope.launch {
            getAppIconSettingsUseCase().collect { appIconSettings ->
                _uiState.update {
                    it.copy(
                        appIconSettings = appIconSettings,
                        initialAppIconSettings = appIconSettings,
                    )
                }
            }
        }
    }

    fun changeAppIconSize(appIconSize: Float) {
        _uiState.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    appIconSize = appIconSize,
                ),
                isSaveButtonEnabled = appIconSize != it.initialAppIconSettings.appIconSize,
            )
        }
    }

    fun changeAppIconShape(appIconShape: AppIconShape) {
        _uiState.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    appIconShape = appIconShape,
                ),
                isSaveButtonEnabled = appIconShape != it.initialAppIconSettings.appIconShape,
            )
        }
    }

    fun changeRoundedCornerPercent(roundedCornerPercent: Float) {
        _uiState.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    roundedCornerPercent = roundedCornerPercent,
                ),
                isSaveButtonEnabled = roundedCornerPercent != it.initialAppIconSettings.roundedCornerPercent,
            )
        }
    }

    fun changeIsAppNameShown(isAppNameShown: Boolean) {
        _uiState.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    isAppNameShown = isAppNameShown,
                ),
                isSaveButtonEnabled = isAppNameShown != it.initialAppIconSettings.isAppNameShown,
            )
        }
    }

    fun changeIsFavoriteAppBackgroundShown(isFavoriteAppBackgroundShown: Boolean) {
        _uiState.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    isFavoriteAppBackgroundShown = isFavoriteAppBackgroundShown,
                ),
                isSaveButtonEnabled = isFavoriteAppBackgroundShown != it.initialAppIconSettings.isFavoriteAppBackgroundShown,
            )
        }
    }

    fun saveAppIconSettings(
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
                saveAppIconSettingsUseCase(uiState.value.appIconSettings)
                onSaveSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save app icon settings", e)
                onSaveFailure()
            }
        }
    }

    private companion object {
        const val TAG = "AppIconSettingsViewModel"
    }
}
