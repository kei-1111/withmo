package com.example.withmo.ui.screens.app_icon_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.user_settings.AppIconShape
import com.example.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.app_icon.SaveAppIconSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppIconSettingsViewModel @Inject constructor(
    private val getAppIconSettingsUseCase: GetAppIconSettingsUseCase,
    private val saveAppIconSettingsUseCase: SaveAppIconSettingsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppIconSettingsUiState())
    val uiState: StateFlow<AppIconSettingsUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AppIconSettingsUiEvent>()
    val uiEvent: SharedFlow<AppIconSettingsUiEvent> = _uiEvent

    init {
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

    fun changeAppIconHorizontalSpacing(appIconHorizontalSpacing: Float) {
        _uiState.update {
            it.copy(
                appIconSettings = it.appIconSettings.copy(
                    appIconHorizontalSpacing = appIconHorizontalSpacing,
                ),
                isSaveButtonEnabled = appIconHorizontalSpacing != it.initialAppIconSettings.appIconHorizontalSpacing,
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

    fun saveAppIconSettings() {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                saveAppIconSettingsUseCase(uiState.value.appIconSettings)
                _uiEvent.emit(AppIconSettingsUiEvent.SaveSuccess)
            } catch (e: Exception) {
                Log.e("AppIconSettingsViewModel", "Failed to save app icon settings", e)
                _uiEvent.emit(AppIconSettingsUiEvent.SaveFailure)
            }
        }
    }

    fun onEvent(event: AppIconSettingsUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
