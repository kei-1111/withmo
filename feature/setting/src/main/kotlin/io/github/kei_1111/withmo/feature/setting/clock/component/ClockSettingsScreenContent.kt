package io.github.kei_1111.withmo.feature.setting.clock.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.clock.ClockSettingsAction
import io.github.kei_1111.withmo.feature.setting.clock.ClockSettingsState

@Composable
internal fun ClockSettingsScreenContent(
    state: ClockSettingsState.Stable,
    onAction: (ClockSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
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

@Composable
@Preview
private fun ClockSettingsScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ClockSettingsScreenContent(
            state = ClockSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun ClockSettingsScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ClockSettingsScreenContent(
            state = ClockSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
