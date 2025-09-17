package io.github.kei_1111.withmo.feature.setting.root

import android.app.WallpaperManager
import android.content.Intent
import android.provider.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import javax.inject.Inject

@Suppress("CyclomaticComplexMethod")
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val permissionChecker: PermissionChecker,
) : BaseViewModel<SettingsViewModelState, SettingsState, SettingsAction, SettingsEffect>() {

    override fun createInitialViewModelState() = SettingsViewModelState()
    override fun createInitialState(): SettingsState = SettingsState()

    override fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnSettingsScreenLifecycleChanged -> {
                updateViewModelState { copy(isDefaultHomeApp = action.isDefaultHomeApp) }
            }

            is SettingsAction.OnNavigateHomeAppSettingButtonClick -> {
                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                sendEffect(SettingsEffect.OpenHomeAppSettings(intent))
            }

            is SettingsAction.OnNavigateClockSettingsButtonClick -> {
                sendEffect(SettingsEffect.NavigateClockSettings)
            }

            is SettingsAction.OnNavigateAppIconSettingsButtonClick -> {
                sendEffect(SettingsEffect.NavigateAppIconSettings)
            }

            is SettingsAction.OnNavigateFavoriteAppSettingsButtonClick -> {
                sendEffect(SettingsEffect.NavigateFavoriteAppSettings)
            }

            is SettingsAction.OnNavigateSideButtonSettingsButtonClick -> {
                sendEffect(SettingsEffect.NavigateSideButtonSettings)
            }

            is SettingsAction.OnNavigateSortSettingsButtonClick -> {
                sendEffect(SettingsEffect.NavigateSortSettings)
            }

            is SettingsAction.OnNavigateNotificationSettingsButtonClick -> {
                if (permissionChecker.isNotificationListenerEnabled()) {
                    sendEffect(SettingsEffect.NavigateNotificationSettings)
                } else {
                    updateViewModelState { copy(isNotificationPermissionDialogVisible = true) }
                }
            }

            is SettingsAction.OnNavigateWallpaperSettingsButtonClick -> {
                val intent = Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER)
                sendEffect(SettingsEffect.OpenWallpaperSettings(intent))
            }

            is SettingsAction.OnNavigateThemeSettingsButtonClick -> {
                sendEffect(SettingsEffect.NavigateThemeSettings)
            }

            is SettingsAction.OnBackButtonClick -> {
                sendEffect(SettingsEffect.NavigateBack)
            }

            is SettingsAction.OnNotificationPermissionDialogConfirm -> {
                updateViewModelState { copy(isNotificationPermissionDialogVisible = false) }
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                sendEffect(SettingsEffect.RequestNotificationListenerPermission(intent))
            }

            is SettingsAction.OnNotificationPermissionDialogDismiss -> {
                updateViewModelState { copy(isNotificationPermissionDialogVisible = false) }
            }

            is SettingsAction.OnNotificationListenerPermissionResult -> {
                if (permissionChecker.isNotificationListenerEnabled()) {
                    sendEffect(SettingsEffect.NavigateNotificationSettings)
                } else {
                    sendEffect(SettingsEffect.ShowToast("通知機能を使用するには\n通知アクセスを許可してください"))
                }
            }
        }
    }

    private companion object {
        const val TAG = "SettingsViewModel"
    }
}
