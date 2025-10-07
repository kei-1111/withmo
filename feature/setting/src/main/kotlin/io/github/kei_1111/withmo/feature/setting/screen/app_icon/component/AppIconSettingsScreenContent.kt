package io.github.kei_1111.withmo.feature.setting.screen.app_icon.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSlider
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.screen.app_icon.AppIconSettingsAction
import io.github.kei_1111.withmo.feature.setting.screen.app_icon.AppIconSettingsState

@Composable
internal fun AppIconSettingsScreenContent(
    state: AppIconSettingsState.Stable,
    onAction: (AppIconSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AppIconShapePicker(
            selectedAppIconShape = state.appIconSettings.appIconShape,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSlider(
            title = "角丸の大きさ",
            value = state.appIconSettings.roundedCornerPercent,
            onValueChange = { onAction(AppIconSettingsAction.OnRoundedCornerPercentSliderChange(it)) },
            valueRange = AppConstants.MIN_ROUNDED_CORNER_PERCENT..AppConstants.MAX_ROUNDED_CORNER_PERCENT,
            enabled = state.appIconSettings.appIconShape == AppIconShape.RoundedCorner,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun AppIconSettingsScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        AppIconSettingsScreenContent(
            state = AppIconSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun AppIconSettingsScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        AppIconSettingsScreenContent(
            state = AppIconSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
