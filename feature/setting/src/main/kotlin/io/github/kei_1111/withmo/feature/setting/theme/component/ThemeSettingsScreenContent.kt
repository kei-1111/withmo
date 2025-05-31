package io.github.kei_1111.withmo.feature.setting.theme.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
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
