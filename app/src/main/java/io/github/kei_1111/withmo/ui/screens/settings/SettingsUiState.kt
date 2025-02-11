package io.github.kei_1111.withmo.ui.screens.settings

import io.github.kei_1111.withmo.ui.base.UiState

data class SettingsUiState(
    val isFileAccessPermissionDialogShown: Boolean = false,
    val isDefaultHomeApp: Boolean = true,
) : UiState
