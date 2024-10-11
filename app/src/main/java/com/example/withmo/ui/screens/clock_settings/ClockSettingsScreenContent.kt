package com.example.withmo.ui.screens.clock_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.domain.model.user_settings.ClockType
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.WithmoClock
import com.example.withmo.ui.component.WithmoSettingItemWithRadioButton
import com.example.withmo.ui.component.WithmoSettingItemWithSwitch
import com.example.withmo.ui.theme.UiConfig

@Composable
fun ClockSettingsScreenContent(
    uiState: ClockSettingsUiState,
    onEvent: (ClockSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(UiConfig.MediumPadding),
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        WithmoSettingItemWithSwitch(
            title = "時計を表示する",
            checked = uiState.clockSettings.isClockShown,
            onCheckedChange = { onEvent(ClockSettingsUiEvent.ChangeIsClockShown(it)) },
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

@Composable
fun ClockTypePicker(
    isClockShown: Boolean,
    selectedClockType: ClockType,
    onEvent: (ClockSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column {
            BodyMediumText(
                text = "時計の種類",
                modifier = Modifier.padding(UiConfig.MediumPadding),
            )
            WithmoSettingItemWithRadioButton(
                item = {
                    WithmoClock(
                        clockType = ClockType.TOP_DATE,
                        dateTimeInfo = DateTimeInfo(),
                        modifier = Modifier.padding(vertical = UiConfig.MediumPadding),
                    )
                },
                selected = ClockType.TOP_DATE == selectedClockType,
                enabled = isClockShown,
                onClick = { onEvent(ClockSettingsUiEvent.ChangeClockType(ClockType.TOP_DATE)) },
                modifier = Modifier.fillMaxWidth(),
            )
            ClockTypePickerDivider()
            WithmoSettingItemWithRadioButton(
                item = {
                    WithmoClock(
                        clockType = ClockType.HORIZONTAL_DATE,
                        dateTimeInfo = DateTimeInfo(),
                        modifier = Modifier.padding(vertical = UiConfig.MediumPadding),
                    )
                },
                selected = ClockType.HORIZONTAL_DATE == selectedClockType,
                enabled = isClockShown,
                onClick = { onEvent(ClockSettingsUiEvent.ChangeClockType(ClockType.HORIZONTAL_DATE)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@NonRestartableComposable
@Composable
private fun ClockTypePickerDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier
            .padding(start = UiConfig.MediumPadding)
            .fillMaxWidth(),
    )
}
