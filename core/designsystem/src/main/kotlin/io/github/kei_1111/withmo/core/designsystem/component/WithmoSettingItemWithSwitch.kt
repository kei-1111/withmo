package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

private const val SwitchScale = 0.75f

@Composable
fun WithmoSettingItemWithSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .height(CommonDimensions.SettingItemHeight),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Paddings.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BodyMediumText(
                text = title,
                modifier = Modifier.weight(Weights.Medium),
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.scale(SwitchScale),
            )
        }
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithSwitchLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoSettingItemWithSwitch(
            title = "設定項目",
            checked = false,
            onCheckedChange = {},
            modifier = Modifier.padding(Paddings.Medium),
        )
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithSwitchDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoSettingItemWithSwitch(
            title = "設定項目",
            checked = true,
            onCheckedChange = {},
            modifier = Modifier.padding(Paddings.Medium),
        )
    }
}
