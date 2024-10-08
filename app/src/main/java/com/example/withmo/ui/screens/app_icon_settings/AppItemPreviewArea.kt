package com.example.withmo.ui.screens.app_icon_settings

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
import com.example.withmo.R
import com.example.withmo.domain.model.user_settings.AppIconSettings
import com.example.withmo.domain.model.user_settings.toShape
import com.example.withmo.ui.component.LabelMediumText
import com.example.withmo.ui.theme.UiConfig

@Composable
fun AppItemPreviewArea(
    appIconSettings: AppIconSettings,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(UiConfig.AppIconPreviewHeight),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(UiConfig.MediumPadding),
            horizontalArrangement = Arrangement.spacedBy(
                appIconSettings.appIconHorizontalSpacing.dp,
                Alignment.CenterHorizontally,
            ),
            verticalAlignment = Alignment.Bottom,
        ) {
            AppItemPreview(
                appIconSettings = appIconSettings,
            )
            AppItemPreview(
                appIconSettings = appIconSettings,
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
            .size((appIconSettings.appIconSize + UiConfig.AppIconPadding).dp),
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
            Spacer(modifier = Modifier.weight(UiConfig.DefaultWeight))
            LabelMediumText(text = previewAppLabel)
        }
    }
}
