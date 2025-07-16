package io.github.kei_1111.withmo.feature.setting.app_icon.component

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
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.feature.setting.R
import io.github.kei_1111.withmo.feature.setting.app_icon.AppIconSettingsScreenDimensions
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment

private const val PreviewAppItemSize = 4

@Composable
internal fun AppItemPreviewArea(
    appIconSettings: AppIconSettings,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(AppIconSettingsScreenDimensions.PreviewHeight),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(PreviewAppItemSize) {
                AppItemPreview(
                    appIconSettings = appIconSettings,
                    modifier = Modifier.weight(Weights.Medium),
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

    val appIconSize = CommonDimensions.AppIconSize

    Column(
        modifier = modifier
            .size(appIconSize + Paddings.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = previewAppIcon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(appIconSize)
                .clip(appIconSettings.appIconShape.toShape(appIconSettings.roundedCornerPercent)),
        )
    }
}

@Composable
@Preview
private fun AppItemPreviewAreaLightPreview() {
    SettingLightPreviewEnvironment {
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
    SettingDarkPreviewEnvironment {
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
    SettingLightPreviewEnvironment {
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
    SettingDarkPreviewEnvironment {
        AppItemPreview(
            appIconSettings = AppIconSettings(
                appIconShape = AppIconShape.Square,
            ),
        )
    }
}
