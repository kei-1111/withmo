package io.github.kei_1111.withmo.feature.setting.clock.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.feature.setting.clock.ClockSettingsAction
import io.github.kei_1111.withmo.feature.setting.clock.ClockSettingsState
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment

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

@Composable
@Preview
private fun ClockSettingsScreenContentLightPreview() {
    SettingLightPreviewEnvironment {
        ClockSettingsScreenContent(
            state = ClockSettingsState(
                clockSettings = ClockSettings(
                    isClockShown = true,
                ),
                isSaveButtonEnabled = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun ClockSettingsScreenContentDarkPreview() {
    SettingDarkPreviewEnvironment {
        ClockSettingsScreenContent(
            state = ClockSettingsState(
                clockSettings = ClockSettings(
                    isClockShown = false,
                ),
                isSaveButtonEnabled = false,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
