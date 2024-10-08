package com.example.withmo.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.theme.UiConfig

@Composable
fun WithmoSettingItemWithSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = UiConfig.MediumPadding),
        ) {
            BodyMediumText(
                text = title,
                modifier = Modifier.padding(vertical = UiConfig.MediumPadding),
            )
            Slider(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                valueRange = valueRange,
            )
        }
    }
}
