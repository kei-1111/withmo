package io.github.kei_1111.withmo.ui.screens.notification_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
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

    fun saveNotificationSettings(
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
                saveNotificationSettingsUseCase(_uiState.value.notificationSettings)
                onSaveSuccess()
            } catch (e: Exception) {
                Log.e("NotificationSettingsViewModel", "Failed to save notification settings", e)
                onSaveFailure()
            }
        }
    }
}
