package io.github.kei_1111.withmo.feature.setting.notification

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.domain.usecase.GetNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModelV2
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase,
    private val permissionChecker: PermissionChecker,
) : BaseViewModelV2<NotificationSettingsViewModelState, NotificationSettingsState, NotificationSettingsAction, NotificationSettingsEffect>() {

    override fun createInitialViewModelState() = NotificationSettingsViewModelState()
    override fun createInitialState() = NotificationSettingsState()

    init {
        observeNotificationSettings()
    }

    private fun observeNotificationSettings() {
        viewModelScope.launch {
            getNotificationSettingsUseCase().collect { notificationSettings ->
                updateViewModelState {
                    copy(
                        notificationSettings = notificationSettings,
                        initialNotificationSettings = notificationSettings,
                    )
                }
            }
        }
    }

    override fun onAction(action: NotificationSettingsAction) {
        when (action) {
            is NotificationSettingsAction.OnIsNotificationAnimationEnabledSwitchChange -> {
                updateViewModelState {
                    val updatedNotificationSettings = notificationSettings.copy(
                        isNotificationAnimationEnabled = action.isNotificationAnimationEnabled,
                    )
                    copy(notificationSettings = updatedNotificationSettings)
                }
            }

            is NotificationSettingsAction.OnIsNotificationBadgeEnabledSwitchChange -> {
                updateViewModelState {
                    val updatedNotificationSettings = notificationSettings.copy(isNotificationBadgeEnabled = action.isNotificationBadgeEnabled)
                    copy(notificationSettings = updatedNotificationSettings)
                }
            }

            is NotificationSettingsAction.OnSaveButtonClick -> {
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

            is NotificationSettingsAction.OnBackButtonClick -> {
                sendEffect(NotificationSettingsEffect.NavigateBack)
            }

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
