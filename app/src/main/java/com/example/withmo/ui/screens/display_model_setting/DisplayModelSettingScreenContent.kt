package com.example.withmo.ui.screens.display_model_setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.DisplayModelSettingItem
import com.example.withmo.ui.theme.UiConfig

@Composable
fun DisplayModelSettingScreenContent(
    uiState: DisplayModelSettingUiState,
    onEvent: (DisplayModelSettingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(UiConfig.MediumPadding),
    ) {
        if (uiState.modelFileList.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
            ) {
                items(uiState.modelFileList) {
                    DisplayModelSettingItem(
                        fileName = it.fileName,
                        downloadDate = it.downloadDate,
                        onClick = { onEvent(DisplayModelSettingUiEvent.SelectModelFile(it)) },
                        selectedModelFile = uiState.selectedModelFile,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        } else {
            BodyMediumText(text = "VRMファイルが見つかりません")
        }
    }
}
