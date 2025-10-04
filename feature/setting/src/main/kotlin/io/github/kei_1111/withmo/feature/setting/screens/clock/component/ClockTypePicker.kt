package io.github.kei_1111.withmo.feature.setting.screens.clock.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.WithmoClock
import io.github.kei_1111.withmo.core.designsystem.component.WithmoHorizontalDivider
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithRadioButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.DateTimeInfo
import io.github.kei_1111.withmo.core.model.user_settings.ClockType
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.screens.clock.ClockSettingsAction

@Composable
internal fun ClockTypePicker(
    isClockShown: Boolean,
    selectedClockType: ClockType,
    onAction: (ClockSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = WithmoTheme.colorScheme.surfaceContainer,
        shape = WithmoTheme.shapes.medium,
    ) {
        Column {
            Text(
                text = "時計の種類",
                modifier = Modifier.padding(16.dp),
                color = WithmoTheme.colorScheme.onSurface,
                style = WithmoTheme.typography.bodyMedium,
            )
            WithmoSettingItemWithRadioButton(
                item = {
                    WithmoClock(
                        clockType = ClockType.TOP_DATE,
                        dateTimeInfo = DateTimeInfo(),
                        modifier = Modifier.padding(vertical = 16.dp),
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
                        modifier = Modifier.padding(vertical = 16.dp),
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
    WithmoHorizontalDivider(modifier = modifier.padding(start = 16.dp))
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
