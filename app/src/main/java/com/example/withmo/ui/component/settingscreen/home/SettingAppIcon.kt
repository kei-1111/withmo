package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.theme.UiConfig

@Composable
fun SettingAppIcon(
    appIconSize: Float,
    setAppIconSize: (Float) -> Unit,
    appIconPadding: Float,
    setAppIconPadding: (Float) -> Unit,
    showAppName: Boolean,
    setShowAppName: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            ExampleAppIcon(
                appIconSize = appIconSize,
                appIconPadding = appIconPadding,
                showAppName = showAppName,
            )
            Text(
                text = "アイコンサイズ",
                style = MaterialTheme.typography.headlineMedium,
            )
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = UiConfig.ExtraSmallPadding),
                value = appIconSize,
                onValueChange = { setAppIconSize(it) },
                valueRange = UiConfig.MinAppIconSize..UiConfig.MaxAppIconSize,
            )
            Text(
                text = "アイコン間隔",
                style = MaterialTheme.typography.headlineMedium,
            )
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = UiConfig.ExtraSmallPadding),
                value = appIconPadding,
                onValueChange = { setAppIconPadding(it) },
                valueRange = UiConfig.MinAppIconPadding..UiConfig.MaxAppIconPadding,
            )
            UserSettingWithSwitch(
                title = "アプリ名を表示",
                checked = showAppName,
                onCheckedChange = { setShowAppName(it) },
            )
        }
    }
}
