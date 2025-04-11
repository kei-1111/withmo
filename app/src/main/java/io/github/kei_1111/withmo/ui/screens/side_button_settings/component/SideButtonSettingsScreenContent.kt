package io.github.kei_1111.withmo.ui.screens.side_button_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.ui.screens.side_button_settings.SideButtonSettingsUiEvent
import io.github.kei_1111.withmo.ui.screens.side_button_settings.SideButtonSettingsUiState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
internal fun SideButtonSettingsScreenContent(
    uiState: SideButtonSettingsUiState,
    onEvent: (SideButtonSettingsUiEvent) -> Unit,
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
            onCheckedChange = { onEvent(SideButtonSettingsUiEvent.ChangeIsShowScaleSliderButtonShown(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "表示モデル変更ボタンの表示",
            checked = uiState.sideButtonSettings.isOpenDocumentButtonShown,
            onCheckedChange = { onEvent(SideButtonSettingsUiEvent.ChangeIsOpenDocumentButtonShown(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "デフォルトモデル設定ボタンの表示",
            checked = uiState.sideButtonSettings.isSetDefaultModelButtonShown,
            onCheckedChange = { onEvent(SideButtonSettingsUiEvent.ChangeIsSetDefaultModelButtonShown(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
