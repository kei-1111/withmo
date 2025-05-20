package io.github.kei_1111.withmo.ui.screens.onboarding.component.contents

import androidx.activity.compose.BackHandler
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
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingAction
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingState
import io.github.kei_1111.withmo.ui.screens.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.ui.screens.onboarding.component.OnboardingBottomAppBarPreviousButton
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Suppress("LongMethod")
@Composable
internal fun SelectFavoriteAppContent(
    appList: ImmutableList<AppInfo>,
    uiState: OnboardingState,
    onEvent: (OnboardingAction) -> Unit,
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

    BackHandler {
        onEvent(OnboardingAction.OnPreviousButtonClick)
    }

    Column(
        modifier = modifier,
    ) {
        WithmoTopAppBar(
            content = { TitleLargeText("お気に入りアプリは？") },
        )
        Column(
            modifier = Modifier
                .weight(Weights.Medium)
                .padding(
                    top = Paddings.Medium,
                    start = Paddings.Medium,
                    end = Paddings.Medium,
                ),
            verticalArrangement = Arrangement.spacedBy(
                Paddings.Medium,
                Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WithmoSearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.appSearchQuery,
                onValueChange = { onEvent(OnboardingAction.OnAppSearchQueryChange(it)) },
                action = { filterAppList(uiState.appSearchQuery) },
            )
            if (resultAppList.isNotEmpty()) {
                FavoriteAppSelector(
                    appList = resultAppList,
                    favoriteAppList = uiState.selectedAppList,
                    addSelectedAppList = { onEvent(OnboardingAction.OnAllAppListAppClick(it)) },
                    removeSelectedAppList = { onEvent(OnboardingAction.OnFavoriteAppListAppClick(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(Weights.Medium),
                )
            } else {
                CenteredMessage(
                    message = "アプリが見つかりません",
                    modifier = Modifier.weight(Weights.Medium),
                )
            }
        }
        FavoriteAppListRow(
            favoriteAppList = uiState.selectedAppList,
            removeSelectedAppList = { onEvent(OnboardingAction.OnFavoriteAppListAppClick(it)) },
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
    uiState: OnboardingState,
    onEvent: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(Paddings.Medium),
        horizontalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onEvent(OnboardingAction.OnPreviousButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
        OnboardingBottomAppBarNextButton(
            text = if (uiState.selectedAppList.isEmpty()) {
                "スキップ"
            } else {
                "次へ"
            },
            onClick = { onEvent(OnboardingAction.OnNextButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
    }
}
