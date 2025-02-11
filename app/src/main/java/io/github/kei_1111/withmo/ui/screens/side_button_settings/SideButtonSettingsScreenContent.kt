package io.github.kei_1111.withmo.ui.screens.side_button_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.ui.theme.UiConfig

@Composable
fun SideButtonSettingsScreenContent(
    uiState: SideButtonSettingsUiState,
    onEvent: (SideButtonSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(UiConfig.MediumPadding),
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        WithmoSettingItemWithSwitch(
            title = "スケールスライダー表示ボタンの表示",
            checked = uiState.sideButtonSettings.isScaleSliderButtonShown,
            onCheckedChange = { onEvent(SideButtonSettingsUiEvent.ChangeIsScaleSliderButtonShown(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
