package com.example.withmo.ui.screens.setting

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.LabelMediumText
import com.example.withmo.ui.theme.UiConfig

@Composable
fun SettingsScreenContent(
    navigateToNotificationSettingScreen: () -> Unit,
    navigateToClockSettingsScreen: () -> Unit,
    navigateToAppIconSettingsScreen: () -> Unit,
    navigateToHomeScreenContentSettingsScreen: () -> Unit,
    navigateToModelSettingScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(UiConfig.MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        HomeScreenSettings(
            title = "ホーム画面の設定",
            navigateToNotificationSettingScreen = navigateToNotificationSettingScreen,
            navigateToClockSettingsScreen = navigateToClockSettingsScreen,
            navigateToAppIconSettingsScreen = navigateToAppIconSettingsScreen,
            navigateToHomeScreenContentSettingsScreen = navigateToHomeScreenContentSettingsScreen,
        )
        ModelSettings(
            title = "モデルの設定",
            navigateToModelSettingScreen = navigateToModelSettingScreen,
        )
    }
}

@Composable
private fun HomeScreenSettings(
    title: String,
    navigateToNotificationSettingScreen: () -> Unit,
    navigateToClockSettingsScreen: () -> Unit,
    navigateToAppIconSettingsScreen: () -> Unit,
    navigateToHomeScreenContentSettingsScreen: () -> Unit,
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
                    itemName = "通知設定",
                    onClick = navigateToNotificationSettingScreen,
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Default.AccessTime,
                    itemName = "時計設定",
                    onClick = navigateToClockSettingsScreen,
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Default.Apps,
                    itemName = "アプリアイコン設定",
                    onClick = navigateToAppIconSettingsScreen,
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Default.Home,
                    itemName = "ホーム画面コンテンツ設定",
                    onClick = navigateToHomeScreenContentSettingsScreen,
                )
            }
        }
    }
}

@Composable
private fun ModelSettings(
    title: String,
    navigateToModelSettingScreen: () -> Unit,
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
                    itemName = "アバター設定",
                    onClick = navigateToModelSettingScreen,
                )
            }
        }
    }
}

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
) {
    Row(
        modifier = modifier
            .height(UiConfig.SettingsScreenItemHeight)
            .clickable(onClick = onClick)
            .padding(horizontal = UiConfig.MediumPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = itemName,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(UiConfig.SettingsScreenItemIconSize),
        )
        Spacer(
            modifier = Modifier.padding(UiConfig.ExtraSmallPadding),
        )
        BodyMediumText(
            text = itemName,
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Next",
        )
    }
}
