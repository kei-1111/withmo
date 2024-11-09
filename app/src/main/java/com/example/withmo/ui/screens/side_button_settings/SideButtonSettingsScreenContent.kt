package com.example.withmo.ui.screens.side_button_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.component.WithmoSettingItemWithSwitch
import com.example.withmo.ui.theme.UiConfig

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
            title = "スケールスライダー表示ボタンを表示する",
            checked = uiState.sideButtonSettings.isScaleSliderButtonShown,
            onCheckedChange = { onEvent(SideButtonSettingsUiEvent.ChangeIsScaleSliderButtonShown(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "ソートボタンを表示する",
            checked = uiState.sideButtonSettings.isSortButtonShown,
            onCheckedChange = { onEvent(SideButtonSettingsUiEvent.ChangeIsSortButtonShown(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
