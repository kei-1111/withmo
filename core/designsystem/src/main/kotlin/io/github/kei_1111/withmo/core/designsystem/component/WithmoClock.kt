package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.clockTextExtraSmallSize
import io.github.kei_1111.withmo.core.designsystem.component.theme.clockTextLargeSize
import io.github.kei_1111.withmo.core.designsystem.component.theme.clockTextMediumSize
import io.github.kei_1111.withmo.core.designsystem.component.theme.clockTextSmallSize
import io.github.kei_1111.withmo.core.model.DateTimeInfo
import io.github.kei_1111.withmo.core.model.user_settings.ClockType
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
fun WithmoClock(
    clockType: ClockType,
    dateTimeInfo: DateTimeInfo,
    modifier: Modifier = Modifier,
) {
    when (clockType) {
        ClockType.TOP_DATE -> {
            ClockTopDate(
                dateTimeInfo = dateTimeInfo,
                modifier = modifier,
            )
        }

        ClockType.HORIZONTAL_DATE -> {
            ClockHorizontalDate(
                dateTimeInfo = dateTimeInfo,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun ClockTopDate(
    dateTimeInfo: DateTimeInfo,
    modifier: Modifier = Modifier,
    textColor: Color = WithmoTheme.colorScheme.onSurface,
) {
    val dateOffset = 4.dp
    val hourMinuteOffsetY = -4.dp

    Column(
        modifier = modifier
            .width(IntrinsicSize.Max),
    ) {
        Row(
            modifier = Modifier.offset(y = dateOffset),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = dateTimeInfo.year,
                style = clockTextSmallSize,
                color = textColor,
            )
            Text(
                text = dateTimeInfo.month,
                style = clockTextSmallSize,
                color = textColor,
            )
            Text(
                text = dateTimeInfo.day,
                style = clockTextSmallSize,
                color = textColor,
            )
            Text(
                text = dateTimeInfo.dayOfWeek,
                style = clockTextSmallSize,
                color = textColor,
            )
        }
        Row(
            modifier = Modifier.offset(y = hourMinuteOffsetY),
        ) {
            Text(
                text = "${dateTimeInfo.hour}:",
                style = clockTextLargeSize,
                color = textColor,
            )
            Text(
                text = dateTimeInfo.minute,
                style = clockTextLargeSize,
                color = textColor,
            )
        }
    }
}

@Composable
private fun ClockHorizontalDate(
    dateTimeInfo: DateTimeInfo,
    modifier: Modifier = Modifier,
    textColor: Color = WithmoTheme.colorScheme.onSurface,
) {
    val yearOffsetY = 4.dp
    val monthOffsetY = 4.dp
    val dayOffsetY = -4.dp
    val dayOfWeekOffsetY = 5.dp
    val hourMinuteOffsetY = -5.dp

    Column(
        modifier = modifier,
    ) {
        Row {
            Column {
                Text(
                    text = dateTimeInfo.year,
                    style = clockTextExtraSmallSize,
                    color = textColor,
                    modifier = Modifier.offset(y = yearOffsetY),
                )
                Text(
                    text = dateTimeInfo.month,
                    style = clockTextMediumSize,
                    color = textColor,
                    modifier = Modifier.offset(y = monthOffsetY),
                )
                Text(
                    text = dateTimeInfo.day,
                    style = clockTextMediumSize,
                    color = textColor,
                    modifier = Modifier.offset(y = dayOffsetY),
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = dateTimeInfo.dayOfWeek,
                    style = clockTextExtraSmallSize,
                    color = textColor,
                    modifier = Modifier.offset(y = dayOfWeekOffsetY),
                )
                Row(
                    modifier = Modifier.offset(y = hourMinuteOffsetY),
                ) {
                    Text(
                        text = "${dateTimeInfo.hour}:",
                        style = clockTextLargeSize,
                        color = textColor,
                    )
                    Text(
                        text = dateTimeInfo.minute,
                        style = clockTextLargeSize,
                        color = textColor,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WithmoClockTopDateLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoClock(
            clockType = ClockType.TOP_DATE,
            dateTimeInfo = DateTimeInfo(
                year = "2002",
                month = "11",
                day = "11",
                dayOfWeek = "MON",
                hour = "12",
                minute = "34",
            ),
        )
    }
}

@Preview
@Composable
private fun WithmoClockTopDateDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoClock(
            clockType = ClockType.TOP_DATE,
            dateTimeInfo = DateTimeInfo(
                year = "2002",
                month = "11",
                day = "11",
                dayOfWeek = "MON",
                hour = "12",
                minute = "34",
            ),
        )
    }
}

@Preview
@Composable
private fun WithmoClockHorizontalDateLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoClock(
            clockType = ClockType.HORIZONTAL_DATE,
            dateTimeInfo = DateTimeInfo(
                year = "2002",
                month = "11",
                day = "11",
                dayOfWeek = "MON",
                hour = "12",
                minute = "34",
            ),
        )
    }
}

@Preview
@Composable
private fun WithmoClockHorizontalDateDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoClock(
            clockType = ClockType.HORIZONTAL_DATE,
            dateTimeInfo = DateTimeInfo(
                year = "2002",
                month = "11",
                day = "11",
                dayOfWeek = "MON",
                hour = "12",
                minute = "34",
            ),
        )
    }
}
