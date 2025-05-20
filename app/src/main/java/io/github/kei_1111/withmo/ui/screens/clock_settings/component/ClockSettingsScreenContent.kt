package io.github.kei_1111.withmo.ui.screens.clock_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.ui.screens.clock_settings.ClockSettingsAction
import io.github.kei_1111.withmo.ui.screens.clock_settings.ClockSettingsState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
internal fun ClockSettingsScreenContent(
    uiState: ClockSettingsState,
    onEvent: (ClockSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        WithmoSettingItemWithSwitch(
            title = "時計の表示",
            checked = uiState.clockSettings.isClockShown,
            onCheckedChange = { onEvent(ClockSettingsAction.OnIsClockShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        ClockTypePicker(
            isClockShown = uiState.clockSettings.isClockShown,
            selectedClockType = uiState.clockSettings.clockType,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
