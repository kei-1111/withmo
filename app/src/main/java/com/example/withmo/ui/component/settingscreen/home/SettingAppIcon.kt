package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.theme.Typography
import com.example.withmo.until.SMALL_SPACE

@Composable
fun SettingAppIcon(
    appIconSize: Float,
    setAppIconSize: (Float) -> Unit,
    appIconPadding: Float,
    setAppIconPadding: (Float) -> Unit,
    showAppName: Boolean,
    setShowAppName: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
//        UserSettingHeader(
//            category = "アプリアイコン",
//        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ExampleAppIcon(
                appIconSize = appIconSize,
                appIconPadding = appIconPadding,
                showAppName = showAppName,
            )
            Text(
                text = "アイコンサイズ",
                style = MaterialTheme.typography.headlineMedium
            )
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SMALL_SPACE),
                value = appIconSize,
                onValueChange = { setAppIconSize(it) },
                valueRange = 36f..72f
            )
            Text(
                text = "アイコン間隔",
                style = MaterialTheme.typography.headlineMedium
            )
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SMALL_SPACE),
                value = appIconPadding,
                onValueChange = { setAppIconPadding(it) },
                valueRange = 0f..20f
            )
            UserSettingWithSwitch(
                title = "アプリ名を表示",
                checked = showAppName,
                onCheckedChange = { setShowAppName(it) }
            )
        }
    }
}