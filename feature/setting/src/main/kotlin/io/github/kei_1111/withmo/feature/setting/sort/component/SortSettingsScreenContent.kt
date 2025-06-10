package io.github.kei_1111.withmo.feature.setting.sort.component

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
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.sort.SortSettingsAction
import io.github.kei_1111.withmo.feature.setting.sort.SortSettingsState

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun SortSettingsScreenContentLightPreview() {
    SettingLightPreviewEnvironment {
        SortSettingsScreenContent(
            state = SortSettingsState(
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun SortSettingsScreenContentDarkPreview() {
    SettingDarkPreviewEnvironment {
        SortSettingsScreenContent(
            state = SortSettingsState(
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
