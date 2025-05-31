package io.github.kei_1111.withmo.feature.onboarding.component.contents

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
import io.github.kei_1111.withmo.core.designsystem.component.CenteredMessage
import io.github.kei_1111.withmo.core.designsystem.component.TitleLargeText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSearchTextField
import io.github.kei_1111.withmo.core.designsystem.component.WithmoTopAppBar
import io.github.kei_1111.withmo.core.designsystem.component.favorite_settings.FavoriteAppListRow
import io.github.kei_1111.withmo.core.designsystem.component.favorite_settings.FavoriteAppSelector
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.feature.onboarding.OnboardingAction
import io.github.kei_1111.withmo.feature.onboarding.OnboardingState
import io.github.kei_1111.withmo.feature.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.feature.onboarding.component.OnboardingBottomAppBarPreviousButton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Suppress("LongMethod")
@Composable
internal fun SelectFavoriteAppContent(
    appList: ImmutableList<AppInfo>,
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var resultAppList by remember { mutableStateOf(appList) }

    fun filterAppList(query: String) {
        resultAppList =
            appList.filter { it.label.contains(query, ignoreCase = true) }.toPersistentList()
    }

    LaunchedEffect(appList) {
        filterAppList(state.appSearchQuery)
    }

    BackHandler {
        onAction(OnboardingAction.OnPreviousButtonClick)
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
                value = state.appSearchQuery,
                onValueChange = { onAction(OnboardingAction.OnAppSearchQueryChange(it)) },
                action = { filterAppList(state.appSearchQuery) },
            )
            if (resultAppList.isNotEmpty()) {
                FavoriteAppSelector(
                    appList = resultAppList,
                    favoriteAppList = state.selectedAppList,
                    addSelectedAppList = { onAction(OnboardingAction.OnAllAppListAppClick(it)) },
                    removeSelectedAppList = { onAction(OnboardingAction.OnFavoriteAppListAppClick(it)) },
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
            favoriteAppList = state.selectedAppList,
            removeSelectedAppList = { onAction(OnboardingAction.OnFavoriteAppListAppClick(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        SelectFavoriteAppContentBottomAppBar(
            state = state,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SelectFavoriteAppContentBottomAppBar(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(Paddings.Medium),
        horizontalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onAction(OnboardingAction.OnPreviousButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
        OnboardingBottomAppBarNextButton(
            text = if (state.selectedAppList.isEmpty()) {
                "スキップ"
            } else {
                "次へ"
            },
            onClick = { onAction(OnboardingAction.OnNextButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
    }
}
