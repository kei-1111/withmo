package com.example.withmo.ui.screens.theme_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.user_settings.ThemeType
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.LabelMediumText
import com.example.withmo.ui.component.WithmoSettingItemWithRadioButton
import com.example.withmo.ui.theme.UiConfig

@Composable
fun ThemeSettingsScreenContent(
    uiState: ThemeSettingsUiState,
    onEvent: (ThemeSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(UiConfig.MediumPadding),
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        ThemeTypePicker(
            selectedThemeType = uiState.themeSettings.themeType,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ThemeTypePicker(
    selectedThemeType: ThemeType,
    onEvent: (ThemeSettingsUiEvent) -> Unit,
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
                    .height(
                        UiConfig.SettingItemHeight,
                    )
                    .padding(horizontal = UiConfig.MediumPadding),
                contentAlignment = Alignment.CenterStart,
            ) {
                BodyMediumText(text = "テーマ")
            }
            WithmoSettingItemWithRadioButton(
                item = {
                    ThemeTypePickerItem(
                        themeType = ThemeType.TIME_BASED,
                    )
                },
                selected = ThemeType.TIME_BASED == selectedThemeType,
                onClick = { onEvent(ThemeSettingsUiEvent.ChangeThemeType(ThemeType.TIME_BASED)) },
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
                onClick = { onEvent(ThemeSettingsUiEvent.ChangeThemeType(ThemeType.LIGHT)) },
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
                onClick = { onEvent(ThemeSettingsUiEvent.ChangeThemeType(ThemeType.DARK)) },
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
    HorizontalDivider(
        modifier = modifier
            .padding(start = UiConfig.MediumPadding)
            .fillMaxWidth(),
    )
}

@Composable
private fun ThemeTypePickerItem(
    themeType: ThemeType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(UiConfig.SettingItemHeight),
        verticalArrangement = Arrangement.Center,
    ) {
        when (themeType) {
            ThemeType.TIME_BASED -> {
                BodyMediumText(text = "時間帯による自動切り替え")
                LabelMediumText(
                    text = "6時-19時: ライトモード, 19時-6時: ダークモード",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = UiConfig.DisabledContentAlpha),
                )
            }
            ThemeType.LIGHT -> {
                BodyMediumText(text = "ライトモード")
            }
            ThemeType.DARK -> {
                BodyMediumText(text = "ダークモード")
            }
        }
    }
}
