package io.github.kei_1111.withmo.feature.setting.notification

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.domain.usecase.GetNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
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
                updateState {
                    val updatedNotificationSettings = notificationSettings.copy(
                        isNotificationAnimationEnabled = action.isNotificationAnimationEnabled,
                    )
                    copy(
                        notificationSettings = updatedNotificationSettings,
                        isSaveButtonEnabled = updatedNotificationSettings != initialNotificationSettings,
                    )
                }
            }

            is NotificationSettingsAction.OnIsNotificationBadgeEnabledSwitchChange -> {
                updateState {
                    val updatedNotificationSettings = notificationSettings.copy(
                        isNotificationBadgeEnabled = action.isNotificationBadgeEnabled,
                    )
                    copy(
                        notificationSettings = updatedNotificationSettings,
                        isSaveButtonEnabled = updatedNotificationSettings != initialNotificationSettings,
                    )
                }
            }

            is NotificationSettingsAction.OnSaveButtonClick -> saveNotificationSettings()

            is NotificationSettingsAction.OnBackButtonClick -> sendEffect(NotificationSettingsEffect.NavigateBack)

            is NotificationSettingsAction.OnCheckPermissionOnResume -> {
                if (!permissionChecker.isNotificationListenerEnabled()) {
                    sendEffect(NotificationSettingsEffect.NavigateBack)
                }
            }
        }
    }

    private companion object {
        const val TAG = "NotificationSettingsViewModel"
    }
}
