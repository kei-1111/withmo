package io.github.kei_1111.withmo.feature.setting.notification.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.feature.setting.notification.NotificationSettingsAction
import io.github.kei_1111.withmo.feature.setting.notification.NotificationSettingsState

@Composable
internal fun NotificationSettingsScreenContent(
    state: NotificationSettingsState,
    onAction: (NotificationSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        WithmoSettingItemWithSwitch(
            title = "通知の受け取り",
            checked = state.notificationSettings.isNotificationAnimationEnabled,
            onCheckedChange = { onAction(NotificationSettingsAction.OnIsNotificationAnimationEnabledSwitchChange(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
