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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.withmo.R
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.theme.Typography
import com.example.withmo.until.ROUNDED_CORNER_SHAPE

@Composable
fun ModelFile(
    modelFile: ModelFile,
    toHome: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.68f / 1f)
            .clickable {
                modelFile.sendPathToUnity()
                toHome()
            },
        shadowElevation = 5.dp,
        tonalElevation = 5.dp,
        shape = RoundedCornerShape(ROUNDED_CORNER_SHAPE)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_file),
                    contentDescription = null,
                )
                Spacer(
                    modifier = Modifier.width(7.dp)
                )
                Text(
                    text = "vrm",
                    style = Typography.headlineMedium
                )
            }
            Text(
                text = modelFile.fileName,
                style = Typography.bodyMedium
            )
        }
    }
}
