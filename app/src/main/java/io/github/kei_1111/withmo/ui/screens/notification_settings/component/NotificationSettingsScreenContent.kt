package io.github.kei_1111.withmo.ui.screens.notification_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSwitch
import io.github.kei_1111.withmo.ui.screens.notification_settings.NotificationSettingsUiEvent
import io.github.kei_1111.withmo.ui.screens.notification_settings.NotificationSettingsUiState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
fun NotificationSettingsScreenContent(
    uiState: NotificationSettingsUiState,
    onEvent: (NotificationSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        WithmoSettingItemWithSwitch(
            title = "通知の受け取り",
            checked = uiState.notificationSettings.isNotificationAnimationEnabled,
            onCheckedChange = { onEvent(NotificationSettingsUiEvent.ChangeIsNotificationAnimationEnabled(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
