package com.example.withmo.ui.component.settingscreen.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ModelFileList(
    modelFileList: ImmutableList<ModelFile>,
    toHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
        verticalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
        contentPadding = PaddingValues(UiConfig.LargePadding),
    ) {
        items(modelFileList.size) { index ->
            ModelFile(
                modelFile = modelFileList[index],
                toHome = toHome,
            )
        }
    }
}
