package io.github.kei_1111.withmo.ui.screens.settings

import android.content.Intent
import android.provider.Settings
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : BaseViewModel<SettingsState, SettingsAction, SettingsEffect>() {

    override fun createInitialState(): SettingsState = SettingsState()

    override fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnSettingsScreenLifecycleChanged -> updateState { copy(isDefaultHomeApp = action.isDefaultHomeApp) }

            is SettingsAction.OnNavigateHomeAppSettingButtonClick -> {
                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                sendEffect(SettingsEffect.OpenHomeAppSettings(intent))
            }

            is SettingsAction.OnNavigateClockSettingsButtonClick -> sendEffect(SettingsEffect.NavigateClockSettings)

            is SettingsAction.OnNavigateAppIconSettingsButtonClick -> sendEffect(SettingsEffect.NavigateAppIconSettings)

            is SettingsAction.OnNavigateFavoriteAppSettingsButtonClick -> sendEffect(SettingsEffect.NavigateFavoriteAppSettings)

            is SettingsAction.OnNavigateSideButtonSettingsButtonClick -> sendEffect(SettingsEffect.NavigateSideButtonSettings)

            is SettingsAction.OnNavigateSortSettingsButtonClick -> sendEffect(SettingsEffect.NavigateSortSettings)

            is SettingsAction.OnNavigateNotificationSettingsButtonClick -> sendEffect(SettingsEffect.NavigateNotificationSettings)

            is SettingsAction.OnNavigateThemeSettingsButtonClick -> sendEffect(SettingsEffect.NavigateThemeSettings)

            is SettingsAction.OnBackButtonClick -> sendEffect(SettingsEffect.NavigateBack)
        }
    }

    private companion object {
        const val TAG = "SettingsViewModel"
    }
}
