package io.github.kei_1111.withmo.ui.screens.clock_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.screens.clock_settings.ClockSettingsAction
import io.github.kei_1111.withmo.ui.screens.clock_settings.ClockSettingsState

@Composable
internal fun ClockSettingsScreenContent(
    state: ClockSettingsState,
    onAction: (ClockSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        WithmoSettingItemWithSwitch(
            title = "時計の表示",
            checked = state.clockSettings.isClockShown,
            onCheckedChange = { onAction(ClockSettingsAction.OnIsClockShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        ClockTypePicker(
            isClockShown = state.clockSettings.isClockShown,
            selectedClockType = state.clockSettings.clockType,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
