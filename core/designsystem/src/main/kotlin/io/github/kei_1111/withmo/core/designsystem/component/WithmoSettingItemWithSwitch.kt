package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
fun WithmoSettingItemWithSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.height(56.dp),
        color = WithmoTheme.colorScheme.surfaceContainer,
        shape = WithmoTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = WithmoTheme.colorScheme.onSurface,
                style = WithmoTheme.typography.bodyMedium,
            )
            WithmoSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange,
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
        )
    }
}
