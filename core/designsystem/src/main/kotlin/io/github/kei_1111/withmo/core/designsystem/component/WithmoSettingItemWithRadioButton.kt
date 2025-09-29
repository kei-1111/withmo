package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.LocalClickBlocker
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

// RadioButtonを用いて設定項目を作りたいときに使う
// 設定項目はTextだけではないため、itemを@Composableで受け取るようにした
// RadioButtonを使うときは他の項目もあるときのため、Composable側でSurfaceを使わず、親側でSurfaceを使う
@Composable
fun WithmoSettingItemWithRadioButton(
    item: @Composable RowScope.() -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val clickBlocker = LocalClickBlocker.current

    Row(
        modifier = modifier
            .safeClickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item()
        Spacer(modifier = Modifier.weight(1f))
        RadioButton(
            selected = selected,
            onClick = { if (clickBlocker.tryClick()) onClick() },
            enabled = enabled,
        )
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithRadioButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoSettingItemWithRadioButton(
            item = {
                Text(
                    text = "設定項目",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            selected = true,
            onClick = { },
        )
    }
}

@Preview
@Composable
private fun WithmoSettingItemWithRadioButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoSettingItemWithRadioButton(
            item = {
                Text(
                    text = "設定項目",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            selected = true,
            onClick = { },
        )
    }
}
