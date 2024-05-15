package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.ClockMode
import com.example.withmo.ui.theme.Typography
import com.example.withmo.until.MEDIUM_SPACE

@Composable
fun SettingClock(
    showClock: Boolean,
    setShowClock: (Boolean) -> Unit,
    clockMode: ClockMode,
    setClockMode: (ClockMode) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        UserSettingWithSwitch(
            title = "ホームに時計を表示させる",
            checked = showClock,
            onCheckedChange = {
                setShowClock(it)
            }
        )
        Spacer(modifier = Modifier.height(MEDIUM_SPACE))
        Text(
            text = "時計の表示形式",
            style = Typography.headlineMedium
        )
        ExampleClockMode(
            clockModeList = listOf(
                ClockMode.TOP_DATE,
                ClockMode.HORIZONTAL_DATE
            ),
            clockMode = clockMode,
            setClockMode = setClockMode,
            showClock = showClock
        )
    }
}