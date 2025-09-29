package io.github.kei_1111.withmo.feature.setting.theme.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithRadioButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.theme.ThemeSettingsAction

@Composable
internal fun ThemeTypePicker(
    selectedThemeType: ThemeType,
    onAction: (ThemeSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "テーマ",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            WithmoSettingItemWithRadioButton(
                item = {
                    ThemeTypePickerItem(
                        themeType = ThemeType.TIME_BASED,
                    )
                },
                selected = ThemeType.TIME_BASED == selectedThemeType,
                onClick = { onAction(ThemeSettingsAction.OnThemeTypeRadioButtonClick(ThemeType.TIME_BASED)) },
                modifier = Modifier.fillMaxWidth(),
            )
            ThemeTypePickerDivider()
            WithmoSettingItemWithRadioButton(
                item = {
                    ThemeTypePickerItem(
                        themeType = ThemeType.LIGHT,
                    )
                },
                selected = ThemeType.LIGHT == selectedThemeType,
                onClick = { onAction(ThemeSettingsAction.OnThemeTypeRadioButtonClick(ThemeType.LIGHT)) },
                modifier = Modifier.fillMaxWidth(),
            )
            ThemeTypePickerDivider()
            WithmoSettingItemWithRadioButton(
                item = {
                    ThemeTypePickerItem(
                        themeType = ThemeType.DARK,
                    )
                },
                selected = ThemeType.DARK == selectedThemeType,
                onClick = { onAction(ThemeSettingsAction.OnThemeTypeRadioButtonClick(ThemeType.DARK)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@NonRestartableComposable
@Composable
private fun ThemeTypePickerDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(modifier = modifier.padding(start = 16.dp))
}

@Composable
private fun ThemeTypePickerItem(
    themeType: ThemeType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.height(56.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        when (themeType) {
            ThemeType.TIME_BASED -> {
                Text(
                    text = "時間帯による自動切り替え",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "6時-19時: ライトモード, 19時-6時: ダークモード",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            ThemeType.LIGHT -> {
                Text(
                    text = "ライトモード",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            ThemeType.DARK -> {
                Text(
                    text = "ダークモード",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
@Preview
private fun ThemeTypePickerLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ThemeTypePicker(
            selectedThemeType = ThemeType.TIME_BASED,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun ThemeTypePickerDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ThemeTypePicker(
            selectedThemeType = ThemeType.LIGHT,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun ThemeTypePickerItemLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ThemeTypePickerItem(
            themeType = ThemeType.TIME_BASED,
        )
    }
}

@Composable
@Preview
private fun ThemeTypePickerItemDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ThemeTypePickerItem(
            themeType = ThemeType.DARK,
        )
    }
}
