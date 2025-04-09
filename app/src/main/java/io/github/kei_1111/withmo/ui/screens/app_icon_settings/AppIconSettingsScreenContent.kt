package io.github.kei_1111.withmo.ui.screens.app_icon_settings

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
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.common.Constants
import io.github.kei_1111.withmo.common.Constants.DefaultRoundedCornerPercent
import io.github.kei_1111.withmo.common.Constants.MaxRoundedCornerPercent
import io.github.kei_1111.withmo.common.Constants.MinRoundedCornerPercent
import io.github.kei_1111.withmo.domain.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.domain.model.user_settings.toShape
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithRadioButton
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSlider
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.ui.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.ui.theme.dimensions.IconSizes
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
fun AppIconSettingsScreenContent(
    uiState: AppIconSettingsUiState,
    onEvent: (AppIconSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        WithmoSettingItemWithSlider(
            title = "アプリアイコンの大きさ",
            value = uiState.appIconSettings.appIconSize,
            onValueChange = { onEvent(AppIconSettingsUiEvent.ChangeAppIconSize(it)) },
            valueRange = Constants.MinAppIconSize..Constants.MaxAppIconSize,
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
            valueRange = MinRoundedCornerPercent..MaxRoundedCornerPercent,
            enabled = uiState.appIconSettings.appIconShape == AppIconShape.RoundedCorner,
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "アプリ名の表示",
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
                        CommonDimensions.SettingItemHeight,
                    )
                    .padding(horizontal = Paddings.Medium),
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

@NonRestartableComposable
@Composable
fun AppIconShapePickerDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier.padding(
            start = Paddings.Medium + IconSizes.Medium + Paddings.Small,
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
            .height(CommonDimensions.SettingItemHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(IconSizes.Medium)
                .background(
                    MaterialTheme.colorScheme.onSurface,
                    appIconShape.toShape(DefaultRoundedCornerPercent),
                ),
        )
        Spacer(
            modifier = Modifier.padding(Paddings.ExtraSmall),
        )
        BodyMediumText(text = title)
    }
}
