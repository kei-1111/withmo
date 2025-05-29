package io.github.kei_1111.withmo.ui.screens.sort_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.screens.sort_settings.SortSettingsAction
import io.github.kei_1111.withmo.ui.screens.sort_settings.SortSettingsState

@Composable
internal fun SortSettingsScreenContent(
    state: SortSettingsState,
    onAction: (SortSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        SortTypePicker(
            selectedSortType = state.sortSettings.sortType,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
