package io.github.kei_1111.withmo.ui.screens.onboarding.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.component.CenteredMessage
import io.github.kei_1111.withmo.ui.component.TitleLargeText
import io.github.kei_1111.withmo.ui.component.WithmoSearchTextField
import io.github.kei_1111.withmo.ui.component.WithmoTopAppBar
import io.github.kei_1111.withmo.ui.component.favorite_settings.FavoriteAppListRow
import io.github.kei_1111.withmo.ui.component.favorite_settings.FavoriteAppSelector
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingBottomAppBarPreviousButton
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiEvent
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiState
import io.github.kei_1111.withmo.ui.theme.UiConfig
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
                FavoriteAppSelector(
                    appList = resultAppList,
                    favoriteAppList = uiState.selectedAppList,
                    addSelectedAppList = { onEvent(OnboardingUiEvent.AddSelectedAppList(it)) },
                    removeSelectedAppList = { onEvent(OnboardingUiEvent.RemoveSelectedAppList(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(UiConfig.DefaultWeight),
                )
            } else {
                CenteredMessage(
                    message = "アプリが見つかりません",
                    modifier = Modifier.weight(UiConfig.DefaultWeight),
                )
            }
        }
        FavoriteAppListRow(
            favoriteAppList = uiState.selectedAppList,
            removeSelectedAppList = { onEvent(OnboardingUiEvent.RemoveSelectedAppList(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        SelectFavoriteAppContentBottomAppBar(
            uiState = uiState,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SelectFavoriteAppContentBottomAppBar(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(UiConfig.MediumPadding),
        horizontalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onEvent(OnboardingUiEvent.NavigateToPreviousPage) },
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
        OnboardingBottomAppBarNextButton(
            text = "次へ",
            onClick = { onEvent(OnboardingUiEvent.NavigateToNextPage) },
            modifier = Modifier.weight(UiConfig.DefaultWeight),
            enabled = uiState.selectedAppList.isNotEmpty(),
        )
    }
}
