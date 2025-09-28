package io.github.kei_1111.withmo.feature.setting.app_icon

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings

data class AppIconSettingsViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val appIconSettings: AppIconSettings = AppIconSettings(),
    val initialAppIconSettings: AppIconSettings = AppIconSettings(),
    val error: Throwable? = null,
) : ViewModelState<AppIconSettingsState> {

    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> AppIconSettingsState.Idle

        StatusType.LOADING -> AppIconSettingsState.Loading

        StatusType.STABLE -> AppIconSettingsState.Stable(
            appIconSettings = appIconSettings,
            isSaveButtonEnabled = appIconSettings != initialAppIconSettings,
        )

        StatusType.ERROR -> AppIconSettingsState.Error(error ?: Throwable("Unknown error"))
    }
}
