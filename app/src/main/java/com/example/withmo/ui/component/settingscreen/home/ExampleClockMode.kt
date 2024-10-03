package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.ClockMode
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.ui.component.WithmoClock
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ExampleClockMode(
    clockModeList: ImmutableList<ClockMode>,
    clockMode: ClockMode,
    setClockMode: (ClockMode) -> Unit,
    showClock: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(UiConfig.LargePadding),
        horizontalArrangement = Arrangement.Center,
    ) {
        clockModeList.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    enabled = showClock,
                    selected = clockMode == it,
                    onClick = { setClockMode(it) },
                )
                WithmoClock(
                    clockMode = it,
                    dateTimeInfo = DateTimeInfo(),
                )
            }
        }
    }
}
