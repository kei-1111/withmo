package io.github.kei_1111.withmo.ui.screens.side_button_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.ui.screens.side_button_settings.SideButtonSettingsAction
import io.github.kei_1111.withmo.ui.screens.side_button_settings.SideButtonSettingsState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
internal fun SideButtonSettingsScreenContent(
    uiState: SideButtonSettingsState,
    onEvent: (SideButtonSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        WithmoSettingItemWithSwitch(
            title = "スケールスライダー表示ボタンの表示",
            checked = uiState.sideButtonSettings.isShowScaleSliderButtonShown,
            onCheckedChange = { onEvent(SideButtonSettingsAction.OnIsShowScaleSliderButtonShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "表示モデル変更ボタンの表示",
            checked = uiState.sideButtonSettings.isOpenDocumentButtonShown,
            onCheckedChange = { onEvent(SideButtonSettingsAction.OnIsOpenDocumentButtonShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "デフォルトモデル設定ボタンの表示",
            checked = uiState.sideButtonSettings.isSetDefaultModelButtonShown,
            onCheckedChange = { onEvent(SideButtonSettingsAction.OnIsSetDefaultModelButtonShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "カスタマイズボタンの表示",
            checked = uiState.sideButtonSettings.isNavigateSettingsButtonShown,
            onCheckedChange = { onEvent(SideButtonSettingsAction.OnIsNavigateSettingsButtonShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
