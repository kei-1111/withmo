package io.github.kei_1111.withmo.feature.setting.screen.sort.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.screen.sort.SortSettingsAction
import io.github.kei_1111.withmo.feature.setting.screen.sort.SortSettingsState

@Composable
internal fun SortSettingsScreenContent(
    state: SortSettingsState.Stable,
    onAction: (SortSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SortTypePicker(
            selectedSortType = state.sortSettings.sortType,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun SortSettingsScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SortSettingsScreenContent(
            state = SortSettingsState.Stable(
                sortSettings = SortSettings(
                    sortType = SortType.ALPHABETICAL,
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
private fun SortSettingsScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SortSettingsScreenContent(
            state = SortSettingsState.Stable(
                sortSettings = SortSettings(
                    sortType = SortType.USE_COUNT,
                ),
                isSaveButtonEnabled = false,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
