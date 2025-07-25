package io.github.kei_1111.withmo.feature.setting.sort.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.LabelMediumText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSettingItemWithRadioButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.setting.sort.SortSettingsAction

@Composable
internal fun SortTypePicker(
    selectedSortType: SortType,
    onAction: (SortSettingsAction) -> Unit,
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
                BodyMediumText(text = "並び順")
            }
            WithmoSettingItemWithRadioButton(
                item = {
                    SortTypePickerItem(
                        sortType = SortType.ALPHABETICAL,
                    )
                },
                selected = SortType.ALPHABETICAL == selectedSortType,
                onClick = { onAction(SortSettingsAction.OnSortTypeRadioButtonClick(SortType.ALPHABETICAL)) },
                modifier = Modifier.fillMaxWidth(),
            )
            SortTypePickerDivider()
            WithmoSettingItemWithRadioButton(
                item = {
                    SortTypePickerItem(
                        sortType = SortType.USE_COUNT,
                    )
                },
                selected = SortType.USE_COUNT == selectedSortType,
                onClick = { onAction(SortSettingsAction.OnSortTypeRadioButtonClick(SortType.USE_COUNT)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@NonRestartableComposable
@Composable
private fun SortTypePickerDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier
            .padding(start = Paddings.Medium)
            .fillMaxWidth(),
    )
}

@Composable
private fun SortTypePickerItem(
    sortType: SortType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(CommonDimensions.SettingItemHeight),
        verticalArrangement = Arrangement.Center,
    ) {
        when (sortType) {
            SortType.ALPHABETICAL -> {
                BodyMediumText(text = "アルファベット順")
            }
            SortType.USE_COUNT -> {
                BodyMediumText(text = "使用回数順")
                LabelMediumText(
                    text = "端末に保存されているアプリの使用回数で並び替え",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled),
                )
            }
        }
    }
}

@Composable
@Preview
private fun SortTypePickerLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SortTypePicker(
            selectedSortType = SortType.ALPHABETICAL,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun SortTypePickerDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SortTypePicker(
            selectedSortType = SortType.USE_COUNT,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun SortTypePickerItemLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SortTypePickerItem(
            sortType = SortType.ALPHABETICAL,
        )
    }
}

@Composable
@Preview
private fun SortTypePickerItemDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SortTypePickerItem(
            sortType = SortType.USE_COUNT,
        )
    }
}
