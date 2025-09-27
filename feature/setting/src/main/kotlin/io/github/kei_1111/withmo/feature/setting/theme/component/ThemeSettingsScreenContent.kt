package io.github.kei_1111.withmo.feature.setting.theme.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.theme.ThemeSettingsAction
import io.github.kei_1111.withmo.feature.setting.theme.ThemeSettingsState

@Composable
internal fun ThemeSettingsScreenContent(
    state: ThemeSettingsState.Stable,
    onAction: (ThemeSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        ThemeTypePicker(
            selectedThemeType = state.themeSettings.themeType,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun ThemeSettingsScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ThemeSettingsScreenContent(
            state = ThemeSettingsState.Stable(
                themeSettings = ThemeSettings(
                    themeType = ThemeType.TIME_BASED,
                ),
                isSaveButtonEnabled = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun ThemeSettingsScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ThemeSettingsScreenContent(
            state = ThemeSettingsState.Stable(
                themeSettings = ThemeSettings(
                    themeType = ThemeType.DARK,
                ),
                isSaveButtonEnabled = false,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
