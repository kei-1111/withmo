package com.example.withmo.ui.screens.onboarding.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.component.AppItem
import com.example.withmo.ui.component.CenteredMessage
import com.example.withmo.ui.component.TitleLargeText
import com.example.withmo.ui.component.WithmoTextField
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.ui.screens.onboarding.OnboardingBottomAppBarNextButton
import com.example.withmo.ui.screens.onboarding.OnboardingBottomAppBarPreviousButton
import com.example.withmo.ui.screens.onboarding.OnboardingUiEvent
import com.example.withmo.ui.screens.onboarding.OnboardingUiState
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Suppress("LongMethod")
@Composable
fun SelectFavoriteAppContent(
    appList: ImmutableList<AppInfo>,
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var resultAppList by remember { mutableStateOf(appList) }

    fun filterAppList(query: String) {
        resultAppList =
            appList.filter { it.label.contains(query, ignoreCase = true) }.toPersistentList()
    }

    LaunchedEffect(appList) {
        filterAppList(uiState.appSearchQuery)
    }

    Column(
        modifier = modifier,
    ) {
        WithmoTopAppBar(
            content = { TitleLargeText("お気に入りアプリは？") },
        )
        Column(
            modifier = Modifier
                .weight(UiConfig.DefaultWeight)
                .padding(
                    top = UiConfig.MediumPadding,
                    start = UiConfig.MediumPadding,
                    end = UiConfig.MediumPadding,
                ),
            verticalArrangement = Arrangement.spacedBy(
                UiConfig.MediumPadding,
                Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WithmoSearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.appSearchQuery,
                onValueChange = { onEvent(OnboardingUiEvent.OnValueChangeAppSearchQuery(it)) },
                action = { filterAppList(uiState.appSearchQuery) },
            )
            if (resultAppList.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = Modifier.weight(UiConfig.DefaultWeight),
                    columns = GridCells.Fixed(UiConfig.AppListScreenGridColums),
                    verticalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
                    horizontalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
                    contentPadding = PaddingValues(
                        top = UiConfig.ExtraSmallPadding,
                        bottom = UiConfig.MediumPadding,
                    ),
                ) {
                    items(resultAppList.size) { index ->
                        SelectFavoriteAppContentAppItem(
                            appInfo = resultAppList[index],
                            isSelected = uiState.selectedAppList.contains(resultAppList[index]),
                            addSelectedAppList = {
                                onEvent(OnboardingUiEvent.AddSelectedAppList(resultAppList[index]))
                            },
                            removeSelectedAppList = {
                                onEvent(OnboardingUiEvent.RemoveSelectedAppList(resultAppList[index]))
                            },
                            onClick = {
                                if (uiState.selectedAppList.contains(resultAppList[index])) {
                                    onEvent(OnboardingUiEvent.RemoveSelectedAppList(resultAppList[index]))
                                } else {
                                    onEvent(OnboardingUiEvent.AddSelectedAppList(resultAppList[index]))
                                }
                            },
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                alpha = UiConfig.DisabledContentAlpha,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            } else {
                CenteredMessage(
                    message = "アプリが見つかりません",
                    modifier = Modifier.weight(UiConfig.DefaultWeight),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = UiConfig.MediumPadding,
                    vertical = UiConfig.SmallPadding,
                ),
        ) {
            uiState.selectedAppList.forEach {
                SelectFavoriteAppContentAppItem(
                    appInfo = it,
                    isSelected = true,
                    addSelectedAppList = { },
                    removeSelectedAppList = { onEvent(OnboardingUiEvent.RemoveSelectedAppList(it)) },
                    onClick = { onEvent(OnboardingUiEvent.RemoveSelectedAppList(it)) },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.weight(UiConfig.DefaultWeight),
                )
            }

            repeat(UiConfig.FavoriteAppListMaxSize - uiState.selectedAppList.size) {
                EmptyAppItem(
                    modifier = Modifier.weight(UiConfig.DefaultWeight),
                )
            }
        }
        SelectFavoriteAppContentBottomAppBar(
            navigateToPreviousPage = { onEvent(OnboardingUiEvent.NavigateToPreviousPage) },
            navigateToNextPage = { onEvent(OnboardingUiEvent.NavigateToNextPage) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConfig.MediumPadding),
        )
    }
}

@Composable
fun SelectFavoriteAppContentAppItem(
    appInfo: AppInfo,
    isSelected: Boolean,
    addSelectedAppList: () -> Unit,
    removeSelectedAppList: () -> Unit,
    onClick: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AppItem(
            appInfo = appInfo,
            modifier = getAppItemModifier(
                isSelected = isSelected,
                addSelectedAppList = addSelectedAppList,
                removeSelectedAppList = removeSelectedAppList,
                backgroundColor = backgroundColor,
            ),
            onClick = onClick,
        )
    }
}

@Composable
private fun getAppItemModifier(
    isSelected: Boolean,
    addSelectedAppList: () -> Unit,
    removeSelectedAppList: () -> Unit,
    backgroundColor: Color,
): Modifier {
    return if (isSelected) {
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { removeSelectedAppList() }
            .border(
                UiConfig.BorderWidth,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.medium,
            )
            .background(backgroundColor)
            .padding(UiConfig.ExtraSmallPadding)
    } else {
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { addSelectedAppList() }
            .padding(UiConfig.ExtraSmallPadding)
    }
}

@Composable
fun EmptyAppItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .border(
                    UiConfig.BorderWidth,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = UiConfig.DisabledContentAlpha),
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(UiConfig.ExtraSmallPadding),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size((UiConfig.DefaultAppIconSize + UiConfig.AppIconPadding).dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = UiConfig.DisabledContentAlpha),
                )
            }
        }
    }
}

@Composable
private fun WithmoSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    action: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = "アプリを検索",
        action = action,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { action() }),
    )
}

@Composable
private fun SelectFavoriteAppContentBottomAppBar(
    navigateToPreviousPage: () -> Unit,
    navigateToNextPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = navigateToPreviousPage,
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
        OnboardingBottomAppBarNextButton(
            text = "次へ",
            onClick = navigateToNextPage,
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
    }
}
