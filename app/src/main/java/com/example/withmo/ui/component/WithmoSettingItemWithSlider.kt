package com.example.withmo.ui.component

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
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
fun WithmoSettingItemWithSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

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
                interactionSource = interactionSource,
                modifier = Modifier
                    .semantics { contentDescription = "Localized Description" }
                    .requiredSizeIn(
                        minWidth = UiConfig.SliderThumbSize,
                        minHeight = UiConfig.SliderTrackHeight,
                    ),
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
                    )
                },
                track = {
                    val trackModifier = Modifier
                        .height(UiConfig.SliderTrackHeight)
                    SliderDefaults.Track(
                        sliderState = it,
                        modifier = trackModifier,
                    )
                },
            )
        }
    }
}
