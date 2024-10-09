package com.example.withmo.ui.screens.display_model_setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.LabelMediumText
import com.example.withmo.ui.theme.UiConfig

@OptIn(ExperimentalMaterial3Api::class)
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

@Composable
private fun DisplayModelSettingItem(
    fileName: String,
    downloadDate: String,
    onClick: () -> Unit,
    selectedModelFile: ModelFile?,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.height(UiConfig.SettingItemHeight),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
        border = if (selectedModelFile?.fileName == fileName) {
            BorderStroke(UiConfig.BorderWidth, MaterialTheme.colorScheme.primary)
        } else {
            null
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
                .padding(horizontal = UiConfig.MediumPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BodyMediumText(
                text = fileName,
                modifier = Modifier.weight(1f),
            )
            LabelMediumText(
                text = downloadDate,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = UiConfig.DisabledContentAlpha),
            )
        }
    }
}
