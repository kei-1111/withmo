package com.example.withmo.ui.screens.app_icon_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.user_settings.AppIconShape
import com.example.withmo.domain.model.user_settings.toShape
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.WithmoSettingItemWithRadioButton
import com.example.withmo.ui.component.WithmoSettingItemWithSlider
import com.example.withmo.ui.component.WithmoSettingItemWithSwitch
import com.example.withmo.ui.theme.UiConfig

@Composable
fun AppIconSettingsScreenContent(
    uiState: AppIconSettingsUiState,
    onEvent: (AppIconSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(UiConfig.MediumPadding),
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        WithmoSettingItemWithSlider(
            title = "アプリアイコンの大きさ",
            value = uiState.appIconSettings.appIconSize,
            onValueChange = { onEvent(AppIconSettingsUiEvent.ChangeAppIconSize(it)) },
            valueRange = UiConfig.MinAppIconSize..UiConfig.MaxAppIconSize,
            modifier = Modifier.fillMaxWidth(),
        )
        AppIconShapePicker(
            selectedAppIconShape = uiState.appIconSettings.appIconShape,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSlider(
            title = "角丸の大きさ",
            value = uiState.appIconSettings.roundedCornerPercent,
            onValueChange = { onEvent(AppIconSettingsUiEvent.ChangeRoundedCornerPercent(it)) },
            valueRange = UiConfig.MinRoundedCornerPercent..UiConfig.MaxRoundedCornerPercent,
            enabled = uiState.appIconSettings.appIconShape == AppIconShape.RoundedCorner,
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSlider(
            title = "アプリアイコンの間隔",
            value = uiState.appIconSettings.appIconHorizontalSpacing,
            onValueChange = { onEvent(AppIconSettingsUiEvent.ChangeAppIconHorizontalSpacing(it)) },
            valueRange = UiConfig.MinAppIconHorizontalSpacing..UiConfig.MaxAppIconHorizontalSpacing,
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "アプリ名を表示",
            checked = uiState.appIconSettings.isAppNameShown,
            onCheckedChange = { onEvent(AppIconSettingsUiEvent.ChangeIsAppNameShown(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun AppIconShapePicker(
    selectedAppIconShape: AppIconShape,
    onEvent: (AppIconSettingsUiEvent) -> Unit,
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
                BodyMediumText(text = "アプリアイコンの形")
            }
            WithmoSettingItemWithRadioButton(
                item = {
                    AppIconShapePickerItem(
                        appIconShape = AppIconShape.Circle,
                        title = "円形",
                    )
                },
                selected = AppIconShape.Circle == selectedAppIconShape,
                onClick = { onEvent(AppIconSettingsUiEvent.ChangeAppIconShape(AppIconShape.Circle)) },
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
                onClick = { onEvent(AppIconSettingsUiEvent.ChangeAppIconShape(AppIconShape.RoundedCorner)) },
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
                onClick = { onEvent(AppIconSettingsUiEvent.ChangeAppIconShape(AppIconShape.Square)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun AppIconShapePickerDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier.padding(
            start = UiConfig.MediumPadding + UiConfig.SettingsScreenItemIconSize + UiConfig.SmallPadding,
        ),
    )
}

@Composable
fun AppIconShapePickerItem(
    title: String,
    appIconShape: AppIconShape,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(UiConfig.SettingItemHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(UiConfig.SettingsScreenItemIconSize)
                .background(
                    MaterialTheme.colorScheme.onSurface,
                    appIconShape.toShape(UiConfig.DefaultRoundedCornerPercent),
                ),
        )
        Spacer(
            modifier = Modifier.padding(UiConfig.ExtraSmallPadding),
        )
        BodyMediumText(text = title)
    }
}
