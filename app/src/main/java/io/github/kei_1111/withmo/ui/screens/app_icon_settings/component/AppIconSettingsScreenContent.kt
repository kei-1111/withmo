package io.github.kei_1111.withmo.ui.screens.app_icon_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSlider
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.ui.screens.app_icon_settings.AppIconSettingsAction
import io.github.kei_1111.withmo.ui.screens.app_icon_settings.AppIconSettingsState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
internal fun AppIconSettingsScreenContent(
    state: AppIconSettingsState,
    onAction: (AppIconSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        WithmoSettingItemWithSlider(
            title = "アプリアイコンの大きさ",
            value = state.appIconSettings.appIconSize,
            onValueChange = { onAction(AppIconSettingsAction.OnAppIconSizeSliderChange(it)) },
            valueRange = AppConstants.MinAppIconSize..AppConstants.MaxAppIconSize,
            modifier = Modifier.fillMaxWidth(),
        )
        AppIconShapePicker(
            selectedAppIconShape = state.appIconSettings.appIconShape,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSlider(
            title = "角丸の大きさ",
            value = state.appIconSettings.roundedCornerPercent,
            onValueChange = { onAction(AppIconSettingsAction.OnRoundedCornerPercentSliderChange(it)) },
            valueRange = AppConstants.MinRoundedCornerPercent..AppConstants.MaxRoundedCornerPercent,
            enabled = state.appIconSettings.appIconShape == AppIconShape.RoundedCorner,
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "アプリ名の表示",
            checked = state.appIconSettings.isAppNameShown,
            onCheckedChange = { onAction(AppIconSettingsAction.OnIsAppNameShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "お気に入りアプリの背景を表示",
            checked = state.appIconSettings.isFavoriteAppBackgroundShown,
            onCheckedChange = { onAction(AppIconSettingsAction.OnIsFavoriteAppBackgroundShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
