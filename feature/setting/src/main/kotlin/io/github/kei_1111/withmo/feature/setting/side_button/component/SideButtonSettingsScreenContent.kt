package io.github.kei_1111.withmo.feature.setting.side_button.component

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
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
import io.github.kei_1111.withmo.feature.setting.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.SettingLightPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.side_button.SideButtonSettingsAction
import io.github.kei_1111.withmo.feature.setting.side_button.SideButtonSettingsState

@Composable
internal fun SideButtonSettingsScreenContent(
    state: SideButtonSettingsState,
    onAction: (SideButtonSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        WithmoSettingItemWithSwitch(
            title = "スケールスライダー表示ボタンの表示",
            checked = state.sideButtonSettings.isShowScaleSliderButtonShown,
            onCheckedChange = { onAction(SideButtonSettingsAction.OnIsShowScaleSliderButtonShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "表示モデル変更ボタンの表示",
            checked = state.sideButtonSettings.isOpenDocumentButtonShown,
            onCheckedChange = { onAction(SideButtonSettingsAction.OnIsOpenDocumentButtonShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "デフォルトモデル設定ボタンの表示",
            checked = state.sideButtonSettings.isSetDefaultModelButtonShown,
            onCheckedChange = { onAction(SideButtonSettingsAction.OnIsSetDefaultModelButtonShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        WithmoSettingItemWithSwitch(
            title = "カスタマイズボタンの表示",
            checked = state.sideButtonSettings.isNavigateSettingsButtonShown,
            onCheckedChange = { onAction(SideButtonSettingsAction.OnIsNavigateSettingsButtonShownSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun SideButtonSettingsScreenContentLightPreview() {
    SettingLightPreviewEnvironment {
        SideButtonSettingsScreenContent(
            state = SideButtonSettingsState(
                sideButtonSettings = SideButtonSettings(
                    isShowScaleSliderButtonShown = true,
                    isOpenDocumentButtonShown = true,
                    isSetDefaultModelButtonShown = false,
                    isNavigateSettingsButtonShown = true,
                ),
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
private fun SideButtonSettingsScreenContentDarkPreview() {
    SettingDarkPreviewEnvironment {
        SideButtonSettingsScreenContent(
            state = SideButtonSettingsState(
                sideButtonSettings = SideButtonSettings(
                    isShowScaleSliderButtonShown = false,
                    isOpenDocumentButtonShown = false,
                    isSetDefaultModelButtonShown = true,
                    isNavigateSettingsButtonShown = false,
                ),
                isSaveButtonEnabled = false,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
