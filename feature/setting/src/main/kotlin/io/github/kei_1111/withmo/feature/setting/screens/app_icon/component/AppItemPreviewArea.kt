package io.github.kei_1111.withmo.feature.setting.screens.app_icon.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.feature.setting.R

private const val PREVIEW_APP_ITEM_SIZE = 4

@Composable
internal fun AppItemPreviewArea(
    appIconSettings: AppIconSettings,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.height(200.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(PREVIEW_APP_ITEM_SIZE) {
                AppItemPreview(
                    appIconSettings = appIconSettings,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun AppItemPreview(
    appIconSettings: AppIconSettings,
    modifier: Modifier = Modifier,
) {
    val previewAppIcon = R.drawable.withmo_icon_wide

    Column(
        modifier = modifier.size(76.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = previewAppIcon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(appIconSettings.appIconShape.toShape(appIconSettings.roundedCornerPercent)),
        )
    }
}

@Composable
@Preview
private fun AppItemPreviewAreaLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        AppItemPreviewArea(
            appIconSettings = AppIconSettings(
                appIconShape = AppIconShape.Circle,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun AppItemPreviewAreaDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        AppItemPreviewArea(
            appIconSettings = AppIconSettings(
                appIconShape = AppIconShape.RoundedCorner,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun AppItemPreviewLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        AppItemPreview(
            appIconSettings = AppIconSettings(
                appIconShape = AppIconShape.Circle,
            ),
        )
    }
}

@Composable
@Preview
private fun AppItemPreviewDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        AppItemPreview(
            appIconSettings = AppIconSettings(
                appIconShape = AppIconShape.Square,
            ),
        )
    }
}
