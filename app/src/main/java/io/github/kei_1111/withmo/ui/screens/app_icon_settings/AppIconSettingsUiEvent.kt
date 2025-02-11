package io.github.kei_1111.withmo.ui.screens.app_icon_settings

import io.github.kei_1111.withmo.domain.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface AppIconSettingsUiEvent : UiEvent {
    data class ChangeAppIconSize(
        val appIconSize: Float,
    ) : AppIconSettingsUiEvent
    data class ChangeAppIconShape(
        val appIconShape: AppIconShape,
    ) : AppIconSettingsUiEvent
    data class ChangeRoundedCornerPercent(
        val roundedCornerPercent: Float,
    ) : AppIconSettingsUiEvent
    data class ChangeIsAppNameShown(
        val isAppNameShown: Boolean,
    ) : AppIconSettingsUiEvent
    data object Save : AppIconSettingsUiEvent
    data object SaveSuccess : AppIconSettingsUiEvent
    data object SaveFailure : AppIconSettingsUiEvent
    data object NavigateToSettingsScreen : AppIconSettingsUiEvent
}
