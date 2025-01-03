package com.example.withmo.ui.screens.sort_settings

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
import com.example.withmo.domain.model.user_settings.SortType
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.WithmoSettingItemWithRadioButton
import com.example.withmo.ui.theme.UiConfig

@Composable
fun SortSettingsScreenContent(
    uiState: SortSettingsUiState,
    onEvent: (SortSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(UiConfig.MediumPadding),
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        SortTypePicker(
            selectedSortType = uiState.sortSettings.sortType,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SortTypePicker(
    selectedSortType: SortType,
    onEvent: (SortSettingsUiEvent) -> Unit,
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
                        UiConfig.SettingItemHeight,
                    )
                    .padding(horizontal = UiConfig.MediumPadding),
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
                onClick = { onEvent(SortSettingsUiEvent.ChangeSortType(SortType.ALPHABETICAL)) },
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
                onClick = { onEvent(SortSettingsUiEvent.ChangeSortType(SortType.USE_COUNT)) },
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
            .padding(start = UiConfig.MediumPadding)
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
            .height(UiConfig.SettingItemHeight),
        verticalArrangement = Arrangement.Center,
    ) {
        when (sortType) {
            SortType.ALPHABETICAL -> {
                BodyMediumText(text = "アルファベット順")
            }
            SortType.USE_COUNT -> {
                BodyMediumText(text = "使用回数順")
            }
        }
    }
}
