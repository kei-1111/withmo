package io.github.kei_1111.withmo.feature.setting.theme

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings

data class ThemeSettingsViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val themeSettings: ThemeSettings = ThemeSettings(),
    val initialThemeSettings: ThemeSettings = ThemeSettings(),
) : ViewModelState<ThemeSettingsState> {

    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> ThemeSettingsState.Idle

        StatusType.LOADING -> ThemeSettingsState.Loading

        StatusType.STABLE -> ThemeSettingsState.Stable(
            themeSettings = themeSettings,
            isSaveButtonEnabled = themeSettings != initialThemeSettings,
        )

        StatusType.ERROR -> ThemeSettingsState.Error(Throwable("An error occurred in ThemeSettingsViewModelState"))
    }
}
