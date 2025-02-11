package io.github.kei_1111.withmo.ui.screens.display_model_setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.component.display_model_setting.DisplayModelSelector

@Composable
fun DisplayModelSettingScreenContent(
    uiState: DisplayModelSettingUiState,
    onEvent: (DisplayModelSettingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        DisplayModelSelector(
            modelFileList = uiState.modelFileList,
            selectedModelFile = uiState.selectedModelFile,
            selectModelFile = { onEvent(DisplayModelSettingUiEvent.SelectModelFile(it)) },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
