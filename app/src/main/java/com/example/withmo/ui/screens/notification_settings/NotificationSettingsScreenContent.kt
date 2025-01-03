package com.example.withmo.ui.screens.notification_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.component.WithmoSettingItemWithSwitch
import com.example.withmo.ui.theme.UiConfig

@Composable
fun NotificationSettingsScreenContent(
    uiState: NotificationSettingsUiState,
    onEvent: (NotificationSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(UiConfig.MediumPadding),
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        WithmoSettingItemWithSwitch(
            title = "通知の受け取り",
            checked = uiState.notificationSettings.isNotificationAnimationEnabled,
            onCheckedChange = { onEvent(NotificationSettingsUiEvent.ChangeIsNotificationAnimationEnabled(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
