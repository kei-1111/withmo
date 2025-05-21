package io.github.kei_1111.withmo.ui.screens.app_icon_settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.common.AppConstants.DefaultRoundedCornerPercent
import io.github.kei_1111.withmo.domain.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.domain.model.user_settings.toShape
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithRadioButton
import io.github.kei_1111.withmo.ui.screens.app_icon_settings.AppIconSettingsAction
import io.github.kei_1111.withmo.ui.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.ui.theme.dimensions.IconSizes
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings

@Composable
internal fun AppIconShapePicker(
    selectedAppIconShape: AppIconShape,
    onAction: (AppIconSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(
                        CommonDimensions.SettingItemHeight,
                    )
                    .padding(horizontal = Paddings.Medium),
                contentAlignment = Alignment.CenterStart,
            ) {
                BodyMediumText(text = "アプリアイコンの形")
            }
            WithmoSettingItemWithRadioButton(
                item = {
                    AppIconShapePickerItem(
                        appIconShape = AppIconShape.Circle,
                        title = "円形",
                    )
                },
                selected = AppIconShape.Circle == selectedAppIconShape,
                onClick = { onAction(AppIconSettingsAction.OnAppIconShapeRadioButtonClick(AppIconShape.Circle)) },
                modifier = Modifier.fillMaxWidth(),
            )
            AppIconShapePickerDivider()
            WithmoSettingItemWithRadioButton(
                item = {
                    AppIconShapePickerItem(
                        appIconShape = AppIconShape.RoundedCorner,
                        title = "角丸四角形",
                    )
                },
                selected = AppIconShape.RoundedCorner == selectedAppIconShape,
                onClick = { onAction(AppIconSettingsAction.OnAppIconShapeRadioButtonClick(AppIconShape.RoundedCorner)) },
                modifier = Modifier.fillMaxWidth(),
            )
            AppIconShapePickerDivider()
            WithmoSettingItemWithRadioButton(
                item = {
                    AppIconShapePickerItem(
                        appIconShape = AppIconShape.Square,
                        title = "四角形",
                    )
                },
                selected = AppIconShape.Square == selectedAppIconShape,
                onClick = { onAction(AppIconSettingsAction.OnAppIconShapeRadioButtonClick(AppIconShape.Square)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@NonRestartableComposable
@Composable
private fun AppIconShapePickerDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier.padding(
            start = Paddings.Medium + IconSizes.Medium + Paddings.Small,
        ),
    )
}

@Composable
private fun AppIconShapePickerItem(
    title: String,
    appIconShape: AppIconShape,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(CommonDimensions.SettingItemHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(IconSizes.Medium)
                .background(
                    MaterialTheme.colorScheme.onSurface,
                    appIconShape.toShape(DefaultRoundedCornerPercent),
                ),
        )
        Spacer(
            modifier = Modifier.padding(Paddings.ExtraSmall),
        )
        BodyMediumText(text = title)
    }
}
