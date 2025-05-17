package io.github.kei_1111.withmo.ui.screens.app_icon_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.common.AppConstants
import io.github.kei_1111.withmo.domain.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSlider
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.ui.screens.app_icon_settings.AppIconSettingsUiEvent
import io.github.kei_1111.withmo.ui.screens.app_icon_settings.AppIconSettingsUiState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
internal fun AppIconSettingsScreenContent(
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
            onValueChange = { onEvent(AppIconSettingsUiEvent.OnAppIconSizeSliderChange(it)) },
            valueRange = AppConstants.MinAppIconSize..AppConstants.MaxAppIconSize,
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
            onValueChange = { onEvent(AppIconSettingsUiEvent.OnRoundedCornerPercentSliderChange(it)) },
            valueRange = AppConstants.MinRoundedCornerPercent..AppConstants.MaxRoundedCornerPercent,
            enabled = uiState.appIconSettings.appIconShape == AppIconShape.RoundedCorner,
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "アプリ名の表示",
            checked = uiState.appIconSettings.isAppNameShown,
            onCheckedChange = { onEvent(AppIconSettingsUiEvent.OnIsAppNameShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "お気に入りアプリの背景を表示",
            checked = uiState.appIconSettings.isFavoriteAppBackgroundShown,
            onCheckedChange = { onEvent(AppIconSettingsUiEvent.OnIsFavoriteAppBackgroundShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
