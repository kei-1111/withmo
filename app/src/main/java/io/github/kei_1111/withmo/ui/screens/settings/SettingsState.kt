package io.github.kei_1111.withmo.ui.screens.settings

import io.github.kei_1111.withmo.core.featurebase.State

data class SettingsState(
    val isDefaultHomeApp: Boolean = true,
) : State
