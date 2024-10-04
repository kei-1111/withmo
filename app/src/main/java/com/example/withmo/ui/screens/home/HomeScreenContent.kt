package com.example.withmo.ui.screens.home

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.domain.model.SortMode
import com.example.withmo.ui.component.AppItem
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.TitleLargeText
import com.example.withmo.ui.component.WithmoClock
import com.example.withmo.ui.component.WithmoIconButton
import com.example.withmo.ui.theme.UiConfig
import com.unity3d.player.UnityPlayer.UnitySendMessage
import kotlinx.collections.immutable.ImmutableList

@Suppress("LongMethod")
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    context: Context,
    appList: ImmutableList<AppInfo>,
    navigateToSettingScreen: () -> Unit,
    getCurrentTime: () -> DateTimeInfo,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        if (uiState.isShowScaleSlider) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                WithmoIconButton(
                    onClick = {
                        UnitySendMessage("Slidermaneger", "HideSlider", "")
                        onEvent(HomeUiEvent.SetShowScaleSlider(false))
                    },
                    icon = Icons.Default.Close,
                    modifier = Modifier.padding(start = UiConfig.MediumPadding),
                )
            }
        } else {
            if (uiState.currentUserSettings.showClock) {
                WithmoClock(
                    clockMode = uiState.currentUserSettings.clockMode,
                    dateTimeInfo = getCurrentTime(),
                    modifier = Modifier.padding(start = UiConfig.MediumPadding),
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End,
            ) {
                Column(
                    modifier = Modifier.padding(UiConfig.MediumPadding),
                    verticalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
                ) {
                    if (uiState.currentUserSettings.showScaleSliderButton) {
                        WithmoIconButton(
                            onClick = {
                                UnitySendMessage("Slidermaneger", "ShowSlider", "")
                                onEvent(HomeUiEvent.SetShowScaleSlider(true))
                            },
                            icon = Icons.Default.Man,
                        )
                    }
                    if (uiState.currentUserSettings.showSortButton) {
                        WithmoIconButton(
                            onClick = {
                                if (uiState.isExpandPopup) {
                                    onEvent(HomeUiEvent.SetPopupExpanded(false))
                                } else {
                                    onEvent(HomeUiEvent.SetPopupExpanded(true))
                                }
                            },
                            icon = Icons.Default.Tune,
                        )
                    }
                }
                RowAppList(
                    context = context,
                    appList = appList,
                    appIconSize = uiState.currentUserSettings.appIconSize,
                    appIconPadding = uiState.currentUserSettings.appIconPadding,
                    showAppName = uiState.currentUserSettings.showAppName,
                    navigateToSettingScreen = navigateToSettingScreen,
                )
            }
            if (uiState.isExpandPopup) {
                PopupContent(
                    onDismissRequest = { onEvent(HomeUiEvent.SetPopupExpanded(false)) },
                    onSelectSortByUsageOrder = { onEvent(HomeUiEvent.OnSelectSortByUsageOrder) },
                    onSelectSortByAlphabeticalOrder = { onEvent(HomeUiEvent.OnSelectSortByAlphabeticalOrder) },
                    sortMode = uiState.currentUserSettings.sortMode,
                )
            }
        }
    }
}

@Composable
private fun PopupContent(
    onDismissRequest: () -> Unit,
    onSelectSortByUsageOrder: () -> Unit,
    onSelectSortByAlphabeticalOrder: () -> Unit,
    sortMode: SortMode,
    modifier: Modifier = Modifier,
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = modifier
                .width(UiConfig.PopupWidth)
                .padding(horizontal = UiConfig.MediumPadding),
            shape = MaterialTheme.shapes.large,
            shadowElevation = UiConfig.ShadowElevation,
        ) {
            Column(
                modifier = Modifier.padding(UiConfig.MediumPadding),
                verticalArrangement = Arrangement.spacedBy(UiConfig.SmallPadding),
            ) {
                TitleLargeText(text = "アプリの並び順")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = onSelectSortByUsageOrder,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BodyMediumText(
                        text = "使用順",
                        modifier = Modifier.weight(UiConfig.DefaultWeight),
                    )
                    RadioButton(
                        selected = sortMode == SortMode.USE_COUNT,
                        onClick = onSelectSortByUsageOrder,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = onSelectSortByAlphabeticalOrder,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BodyMediumText(
                        text = "ABC順",
                        modifier = Modifier.weight(UiConfig.DefaultWeight),
                    )
                    RadioButton(
                        selected = sortMode == SortMode.ALPHABETICAL,
                        onClick = onSelectSortByAlphabeticalOrder,
                    )
                }
            }
        }
    }
}

@Composable
private fun RowAppList(
    context: Context,
    appList: ImmutableList<AppInfo>,
    appIconSize: Float,
    appIconPadding: Float,
    showAppName: Boolean,
    navigateToSettingScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(appIconPadding.dp),
        contentPadding = PaddingValues(horizontal = UiConfig.MediumPadding),
    ) {
        items(appList.size) { index ->
            AppItem(
                context = context,
                appInfo = appList[index],
                appIconSize = appIconSize,
                showAppName = showAppName,
                navigateToSettingScreen = navigateToSettingScreen,
            )
        }
    }
}
