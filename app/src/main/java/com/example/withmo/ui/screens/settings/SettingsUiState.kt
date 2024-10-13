package com.example.withmo.ui.screens.settings

import com.example.withmo.ui.base.UiState

data class SettingsUiState(
    val isFileAccessPermissionDialogShown: Boolean = false,
    val isDefaultHomeApp: Boolean = true,
) : UiState
