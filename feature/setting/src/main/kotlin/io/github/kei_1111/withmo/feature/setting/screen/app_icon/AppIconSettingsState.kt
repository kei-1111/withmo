package io.github.kei_1111.withmo.feature.setting.screen.app_icon

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings

internal sealed interface AppIconSettingsState : State {
    data object Idle : AppIconSettingsState

    data object Loading : AppIconSettingsState

    data class Stable(
        val appIconSettings: AppIconSettings = AppIconSettings(),
        val isSaveButtonEnabled: Boolean = false,
    ) : AppIconSettingsState

    data class Error(val error: Throwable) : AppIconSettingsState
}
