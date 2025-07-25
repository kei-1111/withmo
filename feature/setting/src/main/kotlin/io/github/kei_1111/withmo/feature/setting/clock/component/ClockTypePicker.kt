package io.github.kei_1111.withmo.feature.setting.clock.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoClock
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithRadioButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.DateTimeInfo
import io.github.kei_1111.withmo.core.model.user_settings.ClockType
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.clock.ClockSettingsAction

@Composable
internal fun ClockTypePicker(
    isClockShown: Boolean,
    selectedClockType: ClockType,
    onAction: (ClockSettingsAction) -> Unit,
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
                onClick = { onAction(ClockSettingsAction.OnClockTypeRadioButtonClick(ClockType.TOP_DATE)) },
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
                onClick = { onAction(ClockSettingsAction.OnClockTypeRadioButtonClick(ClockType.HORIZONTAL_DATE)) },
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

@Composable
@Preview
private fun ClockTypePickerLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ClockTypePicker(
            isClockShown = true,
            selectedClockType = ClockType.TOP_DATE,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun ClockTypePickerDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ClockTypePicker(
            isClockShown = false,
            selectedClockType = ClockType.HORIZONTAL_DATE,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
