package io.github.kei_1111.withmo.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.domain.model.DateTimeInfo
import io.github.kei_1111.withmo.domain.model.user_settings.ClockType
import io.github.kei_1111.withmo.ui.theme.clockTextExtraSmallSize
import io.github.kei_1111.withmo.ui.theme.clockTextLargeSize
import io.github.kei_1111.withmo.ui.theme.clockTextMediumSize
import io.github.kei_1111.withmo.ui.theme.clockTextSmallSize
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

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
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val dateOffset = 4.dp
    val hourMinuteOffsetY = -4.dp

    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.offset(y = dateOffset),
            horizontalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
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
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val yearOffsetY = 5.dp
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
            Spacer(modifier = Modifier.width(Paddings.ExtraSmall))
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
