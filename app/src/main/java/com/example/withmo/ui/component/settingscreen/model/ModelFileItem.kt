package com.example.withmo.ui.component.settingscreen.model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.withmo.R
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.theme.Typography
import com.example.withmo.ui.theme.UiConfig

@Composable
fun ModelFile(
    modelFile: ModelFile,
    toHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(UiConfig.GoldenRatio)
            .clickable {
                modelFile.sendPathToUnity()
                toHome()
            },
        shadowElevation = UiConfig.ShadowElevation,
        tonalElevation = UiConfig.TonalElevation,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(UiConfig.SmallPadding),
            verticalArrangement = Arrangement.spacedBy(
                UiConfig.SmallPadding,
                Alignment.CenterVertically,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_file),
                    contentDescription = null,
                )
                Spacer(
                    modifier = Modifier.width(UiConfig.SmallPadding),
                )
                Text(
                    text = "vrm",
                    style = Typography.headlineMedium,
                )
            }
            Text(
                text = modelFile.fileName,
                style = Typography.bodyMedium,
            )
        }
    }
}
