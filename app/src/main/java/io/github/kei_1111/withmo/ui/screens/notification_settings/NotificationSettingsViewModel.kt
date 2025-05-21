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
) : BaseViewModel<NotificationSettingsState, NotificationSettingsAction, NotificationSettingsEffect>() {

    override fun createInitialState(): NotificationSettingsState = NotificationSettingsState()

    init {
        observeNotificationSettings()
    }

    private fun observeNotificationSettings() {
        viewModelScope.launch {
            getNotificationSettingsUseCase().collect { notificationSettings ->
                _state.update {
                    it.copy(
                        notificationSettings = notificationSettings,
                        initialNotificationSettings = notificationSettings,
                    )
                }
            }
        }
    }

    fun changeIsNotificationAnimationEnable(isNotificationAnimationEnabled: Boolean) {
        val newNotificationSettings = _state.value.notificationSettings.copy(
            isNotificationAnimationEnabled = isNotificationAnimationEnabled,
        )
        _state.update {
            it.copy(
                notificationSettings = newNotificationSettings,
                isSaveButtonEnabled = newNotificationSettings != it.initialNotificationSettings,
            )
        }
    }

    fun changeIsNotificationPermissionDialogShown(isNotificationPermissionDialogShown: Boolean) {
        _state.update {
            it.copy(
                isNotificationPermissionDialogShown = isNotificationPermissionDialogShown,
            )
        }
    }

    fun saveNotificationSettings(
        onSaveSuccess: () -> Unit,
        onSaveFailure: () -> Unit,
    ) {
        _state.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                saveNotificationSettingsUseCase(_state.value.notificationSettings)
                onSaveSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save notification settings", e)
                onSaveFailure()
            }
        }
    }

    override fun onAction(action: NotificationSettingsAction) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }

    private companion object {
        const val TAG = "NotificationSettingsViewModel"
    }
}
