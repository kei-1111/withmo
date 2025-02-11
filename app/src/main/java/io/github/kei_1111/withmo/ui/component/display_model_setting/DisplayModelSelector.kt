package io.github.kei_1111.withmo.ui.component.display_model_setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.domain.model.ModelFile
import io.github.kei_1111.withmo.ui.component.CenteredMessage
import io.github.kei_1111.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DisplayModelSelector(
    modelFileList: ImmutableList<ModelFile>,
    selectedModelFile: ModelFile?,
    selectModelFile: (ModelFile) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (modelFileList.isNotEmpty()) {
        LazyColumn(
            modifier = modifier
                .padding(UiConfig.MediumPadding),
            verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
        ) {
            items(modelFileList) {
                DisplayModelSelectorItem(
                    fileName = it.fileName,
                    downloadDate = it.downloadDate,
                    onClick = { selectModelFile(it) },
                    selectedModelFile = selectedModelFile,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    } else {
        CenteredMessage(
            message = "VRMファイルが見つかりません",
            modifier = modifier,
        )
    }
}
