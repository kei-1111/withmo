package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.withmo.R
import com.example.withmo.domain.model.ClockMode
import com.example.withmo.ui.component.homescreen.Clock

@Composable
fun ExampleClockMode(
    clockModeList: List<ClockMode>,
    clockMode: ClockMode,
    setClockMode: (ClockMode) -> Unit,
    showClock: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.large_padding)),
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.large_padding),
            Alignment.CenterHorizontally
        ),
    ) {
        clockModeList.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    enabled = showClock,
                    modifier = Modifier.size(10.dp),
                    selected = clockMode == it,
                    onClick = { setClockMode(it) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = colors.onSurface.copy(alpha = 0.6f)
                    )
                )
                Spacer(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.small_padding))
                )
                Clock(
                    clockMode = it,
                    year = "2024",
                    month = "01",
                    day = "01",
                    dayOfWeek = "MON",
                    hour = "00",
                    minute = "00"
                )
            }
        }
    }
}