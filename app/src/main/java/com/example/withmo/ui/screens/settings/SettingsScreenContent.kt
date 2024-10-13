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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RadioButtonChecked
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
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.LabelMediumText
import com.example.withmo.ui.theme.UiConfig

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    navigateToDefaultHomeAppSettings: () -> Unit,
    navigateToNotificationSettingsScreen: () -> Unit,
    navigateToClockSettingsScreen: () -> Unit,
    navigateToAppIconSettingsScreen: () -> Unit,
    navigateToSideButtonSettingsScreen: () -> Unit,
    navigateToDisplayModelSettingScreen: () -> Unit,
    navigateToThemeSettingsScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(UiConfig.MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        if (!uiState.isDefaultHomeApp) {
            HomeAppSettings(
                title = "ホームアプリの設定",
                navigateToDefaultHomeAppSettings = navigateToDefaultHomeAppSettings,
            )
        }
        HomeScreenSettings(
            title = "ホーム画面の設定",
            navigateToNotificationSettingsScreen = navigateToNotificationSettingsScreen,
            navigateToClockSettingsScreen = navigateToClockSettingsScreen,
            navigateToAppIconSettingsScreen = navigateToAppIconSettingsScreen,
            navigateToSideButtonSettingsScreen = navigateToSideButtonSettingsScreen,
        )
        ModelSettings(
            title = "モデルの設定",
            navigateToDisplayModelSettingScreen = navigateToDisplayModelSettingScreen,
        )
        ThemeSettings(
            title = "テーマの設定",
            navigateToThemeSettingScreen = navigateToThemeSettingsScreen,
        )
    }
}

@Composable
private fun HomeAppSettings(
    title: String,
    navigateToDefaultHomeAppSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
    ) {
        LabelMediumText(
            text = title,
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.errorContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Default.ErrorOutline,
                    itemName = "デフォルトホームアプリ",
                    onClick = navigateToDefaultHomeAppSettings,
                    itemColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}

@Composable
private fun HomeScreenSettings(
    title: String,
    navigateToNotificationSettingsScreen: () -> Unit,
    navigateToClockSettingsScreen: () -> Unit,
    navigateToAppIconSettingsScreen: () -> Unit,
    navigateToSideButtonSettingsScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
    ) {
        LabelMediumText(
            text = title,
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Default.Notifications,
                    itemName = "通知",
                    onClick = navigateToNotificationSettingsScreen,
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Default.AccessTime,
                    itemName = "時計",
                    onClick = navigateToClockSettingsScreen,
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Default.Apps,
                    itemName = "アプリアイコン",
                    onClick = navigateToAppIconSettingsScreen,
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Default.RadioButtonChecked,
                    itemName = "サイドボタン",
                    onClick = navigateToSideButtonSettingsScreen,
                )
            }
        }
    }
}

@Composable
private fun ModelSettings(
    title: String,
    navigateToDisplayModelSettingScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
    ) {
        LabelMediumText(
            text = title,
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Default.InsertDriveFile,
                    itemName = "表示モデル",
                    onClick = navigateToDisplayModelSettingScreen,
                )
            }
        }
    }
}

@Composable
private fun ThemeSettings(
    title: String,
    navigateToThemeSettingScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
    ) {
        LabelMediumText(
            text = title,
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Default.Palette,
                    itemName = "テーマ",
                    onClick = navigateToThemeSettingScreen,
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
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Next",
            tint = itemColor,
        )
    }
}
