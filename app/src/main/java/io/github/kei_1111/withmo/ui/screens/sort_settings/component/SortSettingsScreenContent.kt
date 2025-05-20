package io.github.kei_1111.withmo.ui.screens.sort_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.screens.sort_settings.SortSettingsAction
import io.github.kei_1111.withmo.ui.screens.sort_settings.SortSettingsState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
internal fun SortSettingsScreenContent(
    uiState: SortSettingsState,
    onEvent: (SortSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        SortTypePicker(
            selectedSortType = uiState.sortSettings.sortType,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
