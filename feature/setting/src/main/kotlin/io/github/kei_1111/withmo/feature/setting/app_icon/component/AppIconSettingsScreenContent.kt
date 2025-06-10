package io.github.kei_1111.withmo.feature.setting.app_icon.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSlider
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.feature.setting.app_icon.AppIconSettingsAction
import io.github.kei_1111.withmo.feature.setting.app_icon.AppIconSettingsState
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun AppIconSettingsScreenContentLightPreview() {
    SettingLightPreviewEnvironment {
        AppIconSettingsScreenContent(
            state = AppIconSettingsState(
                appIconSettings = AppIconSettings(),
                isSaveButtonEnabled = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun AppIconSettingsScreenContentDarkPreview() {
    SettingDarkPreviewEnvironment {
        AppIconSettingsScreenContent(
            state = AppIconSettingsState(
                appIconSettings = AppIconSettings(),
                isSaveButtonEnabled = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
