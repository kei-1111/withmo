package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.ui.LocalClickBlocker
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

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
    val clickBlocker = LocalClickBlocker.current

    Row(
        modifier = modifier
            .safeClickable { onClick() }
            .padding(horizontal = Paddings.Medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item()
        Spacer(modifier = Modifier.weight(Weights.Medium))
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
                BodyMediumText(text = "設定項目")
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
                BodyMediumText(text = "設定項目")
            },
            selected = true,
            onClick = { },
        )
    }
}
