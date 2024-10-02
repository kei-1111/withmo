package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.example.withmo.ui.theme.UiConfig

@Composable
fun UserSettingWithSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(UiConfig.DefaultWeight),
            style = MaterialTheme.typography.headlineMedium,
        )
        Switch(
            checked = checked,
            modifier = Modifier.scale(UiConfig.SwitchScale),
            onCheckedChange = onCheckedChange,
        )
    }
}
