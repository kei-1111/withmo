package io.github.kei_1111.withmo.ui.screens.clock_settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.domain.model.DateTimeInfo
import io.github.kei_1111.withmo.domain.model.user_settings.ClockType
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.WithmoClock
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithRadioButton
import io.github.kei_1111.withmo.ui.screens.clock_settings.ClockSettingsUiEvent
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
internal fun ClockTypePicker(
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
                modifier = Modifier.padding(Paddings.Medium),
            )
            WithmoSettingItemWithRadioButton(
                item = {
                    WithmoClock(
                        clockType = ClockType.TOP_DATE,
                        dateTimeInfo = DateTimeInfo(),
                        modifier = Modifier.padding(vertical = Paddings.Medium),
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
                        modifier = Modifier.padding(vertical = Paddings.Medium),
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
            .padding(start = Paddings.Medium)
            .fillMaxWidth(),
    )
}
