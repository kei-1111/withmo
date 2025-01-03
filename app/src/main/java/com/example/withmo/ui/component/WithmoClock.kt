package com.example.withmo.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.domain.model.user_settings.ClockType
import com.example.withmo.ui.theme.UiConfig
import com.example.withmo.ui.theme.clockTextExtraSmallSize
import com.example.withmo.ui.theme.clockTextLargeSize
import com.example.withmo.ui.theme.clockTextMediumSize
import com.example.withmo.ui.theme.clockTextSmallSize

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
    Column(
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
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
        Spacer(modifier = Modifier.height(UiConfig.ExtraSmallPadding))
        Row {
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
    Column(
        modifier = modifier,
    ) {
        Row {
            Column {
                Text(
                    text = dateTimeInfo.year,
                    style = clockTextExtraSmallSize,
                    color = textColor,
                )
                Text(
                    text = dateTimeInfo.month,
                    style = clockTextMediumSize,
                    color = textColor,
                )
                Text(
                    text = dateTimeInfo.day,
                    style = clockTextMediumSize,
                    color = textColor,
                )
            }
            Spacer(modifier = Modifier.width(UiConfig.SmallPadding))
            Column {
                Text(
                    text = dateTimeInfo.dayOfWeek,
                    style = clockTextSmallSize,
                    color = textColor,
                )
                Spacer(modifier = Modifier.height(UiConfig.ExtraSmallPadding))
                Row {
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
