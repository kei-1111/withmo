package com.example.withmo.ui.screens.notification_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationSettingsUiState())
    val uiState: StateFlow<NotificationSettingsUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<NotificationSettingsUiEvent>()
    val uiEvent: SharedFlow<NotificationSettingsUiEvent> = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            getNotificationSettingsUseCase().collect { notificationSettings ->
                Log.d("NotificationSettingsViewModel init", "notificationSettings: $notificationSettings")
                _uiState.update {
                    it.copy(
                        notificationSettings = notificationSettings,
                        initialNotificationSettings = notificationSettings,
                    )
                }
            }
        }
    }

    fun changeIsNotificationAnimationEnable(isNotificationAnimationEnabled: Boolean) {
        val newNotificationSettings = _uiState.value.notificationSettings.copy(
            isNotificationAnimationEnabled = isNotificationAnimationEnabled,
        )
        _uiState.update {
            it.copy(
                notificationSettings = newNotificationSettings,
                isSaveButtonEnabled = newNotificationSettings != it.initialNotificationSettings,
            )
        }
    }

    fun changeIsNotificationPermissionDialogShown(isNotificationPermissionDialogShown: Boolean) {
        _uiState.update {
            it.copy(
                isNotificationPermissionDialogShown = isNotificationPermissionDialogShown,
            )
        }
    }

    fun saveNotificationSettings() {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                saveNotificationSettingsUseCase(_uiState.value.notificationSettings)
                _uiEvent.emit(NotificationSettingsUiEvent.SaveSuccess)
            } catch (e: Exception) {
                Log.e("NotificationSettingsViewModel", "Failed to save notification settings", e)
                _uiEvent.emit(NotificationSettingsUiEvent.SaveFailure)
            }
        }
    }

    fun onEvent(event: NotificationSettingsUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
