package io.github.kei_1111.withmo.core.designsystem.component

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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

private val SliderThumbSize = 20.dp
private val SliderTrackHeight = 4.dp
private val SliderShadowElevation = 1.dp

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
                minWidth = SliderThumbSize,
                minHeight = SliderTrackHeight,
            ),
        steps = steps,
        thumb = {
            val thumbModifier = Modifier
                .size(SliderThumbSize)
                .shadow(SliderShadowElevation, CircleShape, clip = false)
                .indication(
                    interactionSource = interactionSource,
                    indication = ripple(
                        bounded = false,
                        radius = SliderThumbSize,
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
                .height(SliderTrackHeight)
            SliderDefaults.Track(
                sliderState = it,
                modifier = trackModifier,
                enabled = enabled,
            )
        },
    )
}

@Suppress("MagicNumber")
@Composable
fun WithmoVerticalSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @IntRange(from = 0.toLong()) steps: Int = 0,
) {
    WithmoSlider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        modifier = modifier
            .graphicsLayer {
                rotationZ = 270f
                transformOrigin = TransformOrigin(0f, 0f)
            }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(
                    Constraints(
                        minWidth = constraints.minHeight,
                        maxWidth = constraints.maxHeight,
                        minHeight = constraints.minWidth,
                        maxHeight = constraints.maxHeight,
                    ),
                )
                layout(placeable.height, placeable.width) {
                    placeable.place(-placeable.width, 0)
                }
            },
        enabled = enabled,
        steps = steps,
    )
}

@Preview
@Composable
private fun WithmoSliderLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoSlider(
            value = 0.5f,
            onValueChange = {},
            valueRange = 0f..1f,
            modifier = Modifier.size(200.dp, 50.dp),
        )
    }
}

@Preview
@Composable
private fun WithmoSliderDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoSlider(
            value = 0.5f,
            onValueChange = {},
            valueRange = 0f..1f,
            modifier = Modifier.size(200.dp, 50.dp),
        )
    }
}

@Preview
@Composable
private fun WithmoVerticalSliderLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoVerticalSlider(
            value = 0.5f,
            onValueChange = {},
            valueRange = 0f..1f,
            modifier = Modifier.size(50.dp, 200.dp),
        )
    }
}

@Preview
@Composable
private fun WithmoVerticalSliderDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoVerticalSlider(
            value = 0.5f,
            onValueChange = {},
            valueRange = 0f..1f,
            modifier = Modifier.size(50.dp, 200.dp),
        )
    }
}
