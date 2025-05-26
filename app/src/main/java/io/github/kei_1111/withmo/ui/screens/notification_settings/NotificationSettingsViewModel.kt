package io.github.kei_1111.withmo.ui.screens.notification_settings

import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase,
    private val permissionChecker: PermissionChecker,
) : BaseViewModel<NotificationSettingsState, NotificationSettingsAction, NotificationSettingsEffect>() {

    override fun createInitialState(): NotificationSettingsState = NotificationSettingsState()

    init {
        observeNotificationSettings()
    }

    private fun observeNotificationSettings() {
        viewModelScope.launch {
            getNotificationSettingsUseCase().collect { notificationSettings ->
                updateState {
                    copy(
                        notificationSettings = notificationSettings,
                        initialNotificationSettings = notificationSettings,
                    )
                }
            }
        }
    }

    private fun saveNotificationSettings() {
        updateState { copy(isSaveButtonEnabled = false) }
        viewModelScope.launch {
            try {
                saveNotificationSettingsUseCase(state.value.notificationSettings)
                sendEffect(NotificationSettingsEffect.ShowToast("保存しました"))
                sendEffect(NotificationSettingsEffect.NavigateBack)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save notification settings", e)
                sendEffect(NotificationSettingsEffect.ShowToast("保存に失敗しました"))
            }
        }
    }

    override fun onAction(action: NotificationSettingsAction) {
        when (action) {
            is NotificationSettingsAction.OnIsNotificationAnimationEnabledSwitchChange -> {
                if (permissionChecker.isNotificationListenerEnabled()) {
                    updateState {
                        val updatedNotificationSettings = notificationSettings.copy(
                            isNotificationAnimationEnabled = action.isNotificationAnimationEnabled,
                        )
                        copy(
                            notificationSettings = updatedNotificationSettings,
                            isSaveButtonEnabled = updatedNotificationSettings != initialNotificationSettings,
                        )
                    }
                } else {
                    updateState {
                        val updatedNotificationSettings = notificationSettings.copy(isNotificationAnimationEnabled = false)
                        copy(
                            notificationSettings = updatedNotificationSettings,
                            isNotificationPermissionDialogShown = true,
                            isSaveButtonEnabled = updatedNotificationSettings != initialNotificationSettings,
                        )
                    }
                }
            }

            is NotificationSettingsAction.OnNotificationPermissionDialogConfirm -> {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                sendEffect(NotificationSettingsEffect.RequestNotificationListenerPermission(intent))
                updateState { copy(isNotificationPermissionDialogShown = false) }
            }

            is NotificationSettingsAction.OnNotificationPermissionDialogDismiss -> {
                updateState { copy(isNotificationPermissionDialogShown = false) }
                sendEffect(NotificationSettingsEffect.ShowToast("通知アニメーションを有効にするには\n通知アクセスを許可してください"))
            }

            is NotificationSettingsAction.OnSaveButtonClick -> saveNotificationSettings()

            is NotificationSettingsAction.OnBackButtonClick -> sendEffect(NotificationSettingsEffect.NavigateBack)

            is NotificationSettingsAction.OnNotificationListenerPermissionResult -> {
                if (permissionChecker.isNotificationListenerEnabled()) {
                    updateState {
                        val updatedNotificationSettings = notificationSettings.copy(isNotificationAnimationEnabled = true)
                        copy(
                            notificationSettings = updatedNotificationSettings,
                            isSaveButtonEnabled = updatedNotificationSettings != initialNotificationSettings,
                        )
                    }
                } else {
                    updateState {
                        val updatedNotificationSettings = notificationSettings.copy(isNotificationAnimationEnabled = false)
                        copy(
                            notificationSettings = updatedNotificationSettings,
                            isSaveButtonEnabled = updatedNotificationSettings != initialNotificationSettings,
                        )
                    }
                }
            }
        }
    }

    private companion object {
        const val TAG = "NotificationSettingsViewModel"
    }
}
