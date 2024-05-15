package com.example.withmo.ui.component.settingscreen.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.until.CONTENT_PADDING

@Composable
fun ModelFileList(
    modelFileList: MutableList<ModelFile>,
    toHome: () -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(CONTENT_PADDING),
        verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING),
        contentPadding = PaddingValues(CONTENT_PADDING)
    ) {
        items(modelFileList.size) { index ->
            ModelFile(
                modelFile = modelFileList[index],
                toHome = toHome
            )
        }
    }
}

