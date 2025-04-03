package io.github.kei_1111.withmo.ui.screens.settings

import io.github.kei_1111.withmo.domain.model.Screen
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface SettingsUiEvent : UiEvent {
    data class OnNavigate(val screen: Screen) : SettingsUiEvent
    data object SetDefaultHomeApp : SettingsUiEvent
}
