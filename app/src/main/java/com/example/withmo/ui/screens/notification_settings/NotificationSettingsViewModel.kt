package com.example.withmo.ui.screens.notification_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCase
import com.example.withmo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase,
) : BaseViewModel<NotificationSettingsUiState, NotificationSettingsUiEvent>() {

    override fun createInitialState(): NotificationSettingsUiState = NotificationSettingsUiState()

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
}
