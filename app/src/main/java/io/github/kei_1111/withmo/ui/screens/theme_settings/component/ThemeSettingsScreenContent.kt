package io.github.kei_1111.withmo.ui.screens.theme_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.screens.theme_settings.ThemeSettingsAction
import io.github.kei_1111.withmo.ui.screens.theme_settings.ThemeSettingsState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

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
