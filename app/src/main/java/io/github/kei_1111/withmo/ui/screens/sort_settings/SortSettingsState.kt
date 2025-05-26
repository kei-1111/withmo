package io.github.kei_1111.withmo.ui.screens.sort_settings

import io.github.kei_1111.withmo.domain.model.user_settings.SortSettings
import io.github.kei_1111.withmo.ui.base.State

data class SortSettingsState(
    val sortSettings: SortSettings = SortSettings(),
    val initialSortSettings: SortSettings = SortSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : State
