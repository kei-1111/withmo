package io.github.kei_1111.withmo.feature.setting.notification

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.domain.usecase.GetNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase,
    private val permissionChecker: PermissionChecker,
) : StatefulBaseViewModel<NotificationSettingsViewModelState, NotificationSettingsState, NotificationSettingsAction, NotificationSettingsEffect>() {

    override fun createInitialViewModelState() = NotificationSettingsViewModelState()
    override fun createInitialState() = NotificationSettingsState.Idle

    private val notificationSettingsDataStream = getNotificationSettingsUseCase()

    init {
        viewModelScope.launch {
            updateViewModelState { copy(statusType = NotificationSettingsViewModelState.StatusType.LOADING) }
            notificationSettingsDataStream.collect { result ->
                result
                    .onSuccess { notificationSettings ->
                        updateViewModelState {
                            copy(
                                statusType = NotificationSettingsViewModelState.StatusType.STABLE,
                                notificationSettings = notificationSettings,
                                initialNotificationSettings = notificationSettings,
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = NotificationSettingsViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
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
                        saveNotificationSettingsUseCase(_viewModelState.value.notificationSettings)
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
