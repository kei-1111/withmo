package io.github.kei_1111.withmo.core.designsystem.component

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemDarkPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemLightPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings

@Composable
fun WithmoSettingItemWithSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @IntRange(from = 0.toLong()) steps: Int = 0,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Paddings.Medium),
        ) {
            BodyMediumText(
                text = title,
                modifier = Modifier.padding(vertical = Paddings.Medium),
            )
            WithmoSlider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                enabled = enabled,
                steps = steps,
            )
        }
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithSliderLightPreview() {
    DesignSystemLightPreviewEnvironment {
        WithmoSettingItemWithSlider(
            title = "設定項目",
            value = 0.5f,
            onValueChange = {},
            valueRange = 0f..1f,
            modifier = Modifier.padding(Paddings.Medium),
        )
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithSliderDarkPreview() {
    DesignSystemDarkPreviewEnvironment {
        WithmoSettingItemWithSlider(
            title = "設定項目",
            value = 0.5f,
            onValueChange = {},
            valueRange = 0f..1f,
            modifier = Modifier.padding(Paddings.Medium),
        )
    }
}
