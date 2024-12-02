package com.example.withmo.ui.component

import androidx.annotation.IntRange
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.withmo.ui.theme.UiConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithmoSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @IntRange(from = 0.toLong()) steps: Int = 0,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Slider(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        valueRange = valueRange,
        interactionSource = interactionSource,
        modifier = modifier
            .semantics { contentDescription = "Localized Description" }
            .requiredSizeIn(
                minWidth = UiConfig.SliderThumbSize,
                minHeight = UiConfig.SliderTrackHeight,
            ),
        steps = steps,
        thumb = {
            val thumbModifier = Modifier
                .size(UiConfig.SliderThumbSize)
                .shadow(UiConfig.SliderShadowElevation, CircleShape, clip = false)
                .indication(
                    interactionSource = interactionSource,
                    indication = ripple(
                        bounded = false,
                        radius = UiConfig.SliderThumbSize,
                    ),
                )
            SliderDefaults.Thumb(
                interactionSource = interactionSource,
                modifier = thumbModifier,
                enabled = enabled,
            )
        },
        track = {
            val trackModifier = Modifier
                .height(UiConfig.SliderTrackHeight)
            SliderDefaults.Track(
                sliderState = it,
                modifier = trackModifier,
                enabled = enabled,
            )
        },
    )
}
