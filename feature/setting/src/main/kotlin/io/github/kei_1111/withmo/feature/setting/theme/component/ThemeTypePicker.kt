package io.github.kei_1111.withmo.feature.setting.theme.component

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.LabelMediumText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithRadioButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment
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
                    .height(
                        CommonDimensions.SettingItemHeight,
                    )
                    .padding(horizontal = Paddings.Medium),
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
    HorizontalDivider(
        modifier = modifier
            .padding(start = Paddings.Medium)
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
            .height(CommonDimensions.SettingItemHeight),
        verticalArrangement = Arrangement.Center,
    ) {
        when (themeType) {
            ThemeType.TIME_BASED -> {
                BodyMediumText(text = "時間帯による自動切り替え")
                LabelMediumText(
                    text = "6時-19時: ライトモード, 19時-6時: ダークモード",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled),
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun ThemeTypePickerLightPreview() {
    SettingLightPreviewEnvironment {
        ThemeTypePicker(
            selectedThemeType = ThemeType.TIME_BASED,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun ThemeTypePickerDarkPreview() {
    SettingDarkPreviewEnvironment {
        ThemeTypePicker(
            selectedThemeType = ThemeType.LIGHT,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun ThemeTypePickerItemLightPreview() {
    SettingLightPreviewEnvironment {
        ThemeTypePickerItem(
            themeType = ThemeType.TIME_BASED,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun ThemeTypePickerItemDarkPreview() {
    SettingDarkPreviewEnvironment {
        ThemeTypePickerItem(
            themeType = ThemeType.DARK,
        )
    }
}
