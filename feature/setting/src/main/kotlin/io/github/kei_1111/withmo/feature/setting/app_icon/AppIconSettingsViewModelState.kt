package io.github.kei_1111.withmo.feature.setting.app_icon

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings

data class AppIconSettingsViewModelState(
    val appIconSettings: AppIconSettings = AppIconSettings(),
    val initialAppIconSettings: AppIconSettings = AppIconSettings(),
) : ViewModelState<AppIconSettingsState> {
    override fun toState() = AppIconSettingsState(
        appIconSettings = appIconSettings,
        isSaveButtonEnabled = appIconSettings != initialAppIconSettings,
    )
}
