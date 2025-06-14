package io.github.kei_1111.withmo.feature.setting.theme.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.theme.ThemeSettingsAction
import io.github.kei_1111.withmo.feature.setting.theme.ThemeSettingsState

@Composable
internal fun ThemeSettingsScreenContent(
    state: ThemeSettingsState,
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun ThemeSettingsScreenContentLightPreview() {
    SettingLightPreviewEnvironment {
        ThemeSettingsScreenContent(
            state = ThemeSettingsState(
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun ThemeSettingsScreenContentDarkPreview() {
    SettingDarkPreviewEnvironment {
        ThemeSettingsScreenContent(
            state = ThemeSettingsState(
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
