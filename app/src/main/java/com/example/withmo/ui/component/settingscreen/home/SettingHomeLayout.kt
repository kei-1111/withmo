package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingHomeLayout(
    showScaleSliderButton: Boolean,
    setScaleSliderButton: (Boolean) -> Unit,
    showSortButton: Boolean,
    setSortButton: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        UserSettingWithSwitch(
            title = "サイズ変更ボタンを表示させる",
            checked = showScaleSliderButton,
            onCheckedChange = {
                setScaleSliderButton(it)
            },
        )
        UserSettingWithSwitch(
            title = "アプリ並び替えボタンを表示させる",
            checked = showSortButton,
            onCheckedChange = {
                setSortButton(it)
            },
        )
    }
}
