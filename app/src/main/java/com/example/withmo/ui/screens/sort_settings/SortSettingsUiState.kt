package com.example.withmo.ui.screens.sort_settings

import com.example.withmo.domain.model.user_settings.SortSettings
import com.example.withmo.ui.base.UiState

data class SortSettingsUiState(
    val sortSettings: SortSettings = SortSettings(),
    val initialSortSettings: SortSettings = SortSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : UiState
