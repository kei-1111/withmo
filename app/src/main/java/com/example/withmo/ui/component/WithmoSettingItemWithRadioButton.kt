package com.example.withmo.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withmo.ui.theme.UiConfig

// RadioButtonを用いて設定項目を作りたいときに使う
// 設定項目はTextだけではないため、itemを@Composableで受け取るようにした
// RadioButtonを使うときは他の項目もあるときのため、Composable側でSurfaceを使わず、親側でSurfaceを使う
@Composable
fun WithmoSettingItemWithRadioButton(
    item: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .clickable(
                onClick = onClick,
            )
            .padding(horizontal = UiConfig.MediumPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item()
        Spacer(modifier = Modifier.weight(UiConfig.DefaultWeight))
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
        )
    }
}
