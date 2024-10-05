package com.example.withmo.ui.screens.notification_setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.component.WithmoSaveButton
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
    ) {
        WithmoSettingItemWithSwitch(
            title = "通知を受け取る",
            checked = uiState.notificationSettings.isNotificationAnimationEnabled,
            onCheckedChange = { onEvent(NotificationSettingsUiEvent.ChangeIsNotificationAnimationEnabled(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.weight(UiConfig.DefaultWeight))
        Spacer(modifier = Modifier.padding(top = UiConfig.MediumPadding))
        WithmoSaveButton(
            onClick = { onEvent(NotificationSettingsUiEvent.Save) },
            enabled = uiState.isSaveButtonEnabled,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
