package com.example.withmo.ui.component.settingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.theme.UiConfig

@Composable
fun ChoosingHome(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .weight(UiConfig.DefaultWeight)
                .height(UiConfig.DividerHeight),
        )
        Divider(
            modifier = Modifier
                .weight(UiConfig.DefaultWeight),
            color = MaterialTheme.colorScheme.surface,
        )
    }
}

@Composable
fun ChoosingModel(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Divider(
            modifier = Modifier.weight(UiConfig.DefaultWeight),
            color = MaterialTheme.colorScheme.surface,
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .weight(UiConfig.DefaultWeight)
                .height(UiConfig.DividerHeight),
        )
    }
}
