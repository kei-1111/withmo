package com.example.withmo.ui.screens.settings

import com.example.withmo.ui.base.UiEvent

sealed interface SettingsUiEvent : UiEvent {
    data object NavigateToDisplayModelSettingScreen : SettingsUiEvent
    data object FileAccessPermissionDialogOnDismiss : SettingsUiEvent
    data object FileAccessPermissionDialogOnConfirm : SettingsUiEvent
}
