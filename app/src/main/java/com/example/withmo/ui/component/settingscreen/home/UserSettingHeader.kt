package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withmo.ui.theme.Typography

@Composable
fun UserSettingHeader(
    category: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = category,
            style = Typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}