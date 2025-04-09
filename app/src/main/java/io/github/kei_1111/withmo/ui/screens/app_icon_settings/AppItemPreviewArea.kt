package io.github.kei_1111.withmo.ui.screens.app_icon_settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.R
import io.github.kei_1111.withmo.domain.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.domain.model.user_settings.toShape
import io.github.kei_1111.withmo.ui.component.LabelMediumText
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

@Composable
fun AppItemPreviewArea(
    appIconSettings: AppIconSettings,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(AppIconSettingsScreenDimensions.PreviewHeight),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Paddings.Medium),
            verticalAlignment = Alignment.Bottom,
        ) {
            AppItemPreview(
                appIconSettings = appIconSettings,
                modifier = Modifier.weight(Weights.Medium),
            )
            AppItemPreview(
                appIconSettings = appIconSettings,
                modifier = Modifier.weight(Weights.Medium),
            )
            AppItemPreview(
                appIconSettings = appIconSettings,
                modifier = Modifier.weight(Weights.Medium),
            )
        }
    }
}

@Composable
private fun AppItemPreview(
    appIconSettings: AppIconSettings,
    modifier: Modifier = Modifier,
) {
    val previewAppIcon = R.drawable.withmo_icon_wide
    val previewAppLabel = "withmo"

    Column(
        modifier = modifier
            .size((appIconSettings.appIconSize + Paddings.AppIconPadding).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = previewAppIcon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(appIconSettings.appIconSize.dp)
                .clip(appIconSettings.appIconShape.toShape(appIconSettings.roundedCornerPercent)),
        )
        if (appIconSettings.isAppNameShown) {
            Spacer(modifier = Modifier.weight(Weights.Medium))
            LabelMediumText(text = previewAppLabel)
        }
    }
}
