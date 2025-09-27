package io.github.kei_1111.withmo.feature.setting.notification.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.notification.NotificationSettingsAction
import io.github.kei_1111.withmo.feature.setting.notification.NotificationSettingsState

@Composable
internal fun NotificationSettingsScreenContent(
    state: NotificationSettingsState.Stable,
    onAction: (NotificationSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        WithmoSettingItemWithSwitch(
            title = "通知アニメーションの実行",
            checked = state.notificationSettings.isNotificationAnimationEnabled,
            onCheckedChange = { onAction(NotificationSettingsAction.OnIsNotificationAnimationEnabledSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )

        WithmoSettingItemWithSwitch(
            title = "バッジ表示",
            checked = state.notificationSettings.isNotificationBadgeEnabled,
            onCheckedChange = { onAction(NotificationSettingsAction.OnIsNotificationBadgeEnabledSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun NotificationSettingsScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        NotificationSettingsScreenContent(
            state = NotificationSettingsState.Stable(
                notificationSettings = NotificationSettings(
                    isNotificationAnimationEnabled = true,
                    isNotificationBadgeEnabled = true,
                ),
                isSaveButtonEnabled = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun NotificationSettingsScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        NotificationSettingsScreenContent(
            state = NotificationSettingsState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
