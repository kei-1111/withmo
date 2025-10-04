package io.github.kei_1111.withmo.feature.setting.screens.app_icon.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.common.AppConstants.DEFAULT_ROUNDED_CORNER_PERCENT
import io.github.kei_1111.withmo.core.designsystem.component.WithmoHorizontalDivider
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithRadioButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.feature.setting.screens.app_icon.AppIconSettingsAction

@Composable
internal fun AppIconShapePicker(
    selectedAppIconShape: AppIconShape,
    onAction: (AppIconSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = WithmoTheme.colorScheme.surfaceContainer,
        shape = WithmoTheme.shapes.medium,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "アプリアイコンの形",
                    color = WithmoTheme.colorScheme.onSurface,
                    style = WithmoTheme.typography.bodyMedium,
                )
            }
            WithmoSettingItemWithRadioButton(
                item = {
                    AppIconShapePickerItem(
                        appIconShape = AppIconShape.Circle,
                        title = "円形",
                    )
                },
                selected = AppIconShape.Circle == selectedAppIconShape,
                onClick = { onAction(AppIconSettingsAction.OnAppIconShapeRadioButtonClick(AppIconShape.Circle)) },
                modifier = Modifier.fillMaxWidth(),
            )
            AppIconShapePickerDivider()
            WithmoSettingItemWithRadioButton(
                item = {
                    AppIconShapePickerItem(
                        appIconShape = AppIconShape.RoundedCorner,
                        title = "角丸四角形",
                    )
                },
                selected = AppIconShape.RoundedCorner == selectedAppIconShape,
                onClick = { onAction(AppIconSettingsAction.OnAppIconShapeRadioButtonClick(AppIconShape.RoundedCorner)) },
                modifier = Modifier.fillMaxWidth(),
            )
            AppIconShapePickerDivider()
            WithmoSettingItemWithRadioButton(
                item = {
                    AppIconShapePickerItem(
                        appIconShape = AppIconShape.Square,
                        title = "四角形",
                    )
                },
                selected = AppIconShape.Square == selectedAppIconShape,
                onClick = { onAction(AppIconSettingsAction.OnAppIconShapeRadioButtonClick(AppIconShape.Square)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@NonRestartableComposable
@Composable
private fun AppIconShapePickerDivider(
    modifier: Modifier = Modifier,
) {
    WithmoHorizontalDivider(modifier = modifier.padding(start = 48.dp))
}

@Composable
private fun AppIconShapePickerItem(
    title: String,
    appIconShape: AppIconShape,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    WithmoTheme.colorScheme.onSurface,
                    appIconShape.toShape(DEFAULT_ROUNDED_CORNER_PERCENT),
                ),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = title,
            color = WithmoTheme.colorScheme.onSurface,
            style = WithmoTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview
private fun AppIconShapePickerLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        AppIconShapePicker(
            selectedAppIconShape = AppIconShape.Circle,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun AppIconShapePickerDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        AppIconShapePicker(
            selectedAppIconShape = AppIconShape.RoundedCorner,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun AppIconShapePickerItemLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        AppIconShapePickerItem(
            title = "円形",
            appIconShape = AppIconShape.Circle,
        )
    }
}

@Composable
@Preview
private fun AppIconShapePickerItemDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        AppIconShapePickerItem(
            title = "角丸四角形",
            appIconShape = AppIconShape.RoundedCorner,
        )
    }
}
