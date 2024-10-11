package com.example.withmo.ui.screens.settings

import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.base.UiEvent

sealed interface SettingsUiEvent : UiEvent {
    data class OnNavigate(val screen: Screen) : SettingsUiEvent
    data object FileAccessPermissionDialogOnDismiss : SettingsUiEvent
    data object FileAccessPermissionDialogOnConfirm : SettingsUiEvent
}
