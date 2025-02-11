package io.github.kei_1111.withmo.ui.component.display_model_setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.domain.model.ModelFile
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.LabelMediumText
import io.github.kei_1111.withmo.ui.theme.UiConfig

@Composable
fun DisplayModelSelectorItem(
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
