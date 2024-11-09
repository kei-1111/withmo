package com.example.withmo.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RadioButtonChecked
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.LabelMediumText
import com.example.withmo.ui.theme.UiConfig

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(UiConfig.MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        if (!uiState.isDefaultHomeApp) {
            HomeAppSettings(onEvent = onEvent)
        }
        HomeScreenSettings(onEvent = onEvent)
        NotificationSettings(onEvent = onEvent)
        ModelSettings(onEvent = onEvent)
        ThemeSettings(onEvent = onEvent)
    }
}

@Composable
private fun HomeAppSettings(
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
    ) {
        LabelMediumText(
            text = "ホームアプリの設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.errorContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Rounded.ErrorOutline,
                    itemName = "デフォルトホームアプリ",
                    onClick = { onEvent(SettingsUiEvent.SetDefaultHomeApp) },
                    itemColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}

@Composable
private fun HomeScreenSettings(
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
    ) {
        LabelMediumText(
            text = "ホーム画面の設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Rounded.AccessTime,
                    itemName = "時計",
                    onClick = { onEvent(SettingsUiEvent.OnNavigate(Screen.ClockSettings)) },
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Rounded.Apps,
                    itemName = "アプリアイコン",
                    onClick = { onEvent(SettingsUiEvent.OnNavigate(Screen.AppIconSettings)) },
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Rounded.Star,
                    itemName = "お気に入りアプリ",
                    onClick = { onEvent(SettingsUiEvent.OnNavigate(Screen.FavoriteAppSettings)) },
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Rounded.RadioButtonChecked,
                    itemName = "サイドボタン",
                    onClick = { onEvent(SettingsUiEvent.OnNavigate(Screen.SideButtonSettings)) },
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Rounded.Tune,
                    itemName = "並び順",
                    onClick = { onEvent(SettingsUiEvent.OnNavigate(Screen.SortSettings)) },
                )
            }
        }
    }
}

@Composable
private fun NotificationSettings(
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
    ) {
        LabelMediumText(
            text = "通知の設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Rounded.Notifications,
                    itemName = "通知",
                    onClick = { onEvent(SettingsUiEvent.OnNavigate(Screen.NotificationSettings)) },
                )
            }
        }
    }
}

@Composable
private fun ModelSettings(
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
    ) {
        LabelMediumText(
            text = "モデルの設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Rounded.InsertDriveFile,
                    itemName = "表示モデル",
                    onClick = { onEvent(SettingsUiEvent.OnNavigate(Screen.DisplayModelSetting)) },
                )
            }
        }
    }
}

@Composable
private fun ThemeSettings(
    onEvent: (SettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
    ) {
        LabelMediumText(
            text = "テーマの設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Rounded.Palette,
                    itemName = "テーマ",
                    onClick = { onEvent(SettingsUiEvent.OnNavigate(Screen.ThemeSettings)) },
                )
            }
        }
    }
}

@NonRestartableComposable
@Composable
private fun SettingItemDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier.padding(
            start = UiConfig.MediumPadding + UiConfig.SettingsScreenItemIconSize + UiConfig.SmallPadding,
        ),
    )
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    itemName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    itemColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = modifier
            .height(UiConfig.SettingItemHeight)
            .clickable { onClick() }
            .padding(horizontal = UiConfig.MediumPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = itemName,
            tint = itemColor,
            modifier = Modifier.size(UiConfig.SettingsScreenItemIconSize),
        )
        Spacer(
            modifier = Modifier.padding(UiConfig.ExtraSmallPadding),
        )
        BodyMediumText(
            text = itemName,
            modifier = Modifier.weight(UiConfig.DefaultWeight),
            color = itemColor,
        )
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = "Next",
            tint = itemColor,
        )
    }
}
