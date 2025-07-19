package io.github.kei_1111.withmo.feature.onboarding.component.contents

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.CenteredMessage
import io.github.kei_1111.withmo.core.designsystem.component.FavoriteAppListRow
import io.github.kei_1111.withmo.core.designsystem.component.FavoriteAppSelector
import io.github.kei_1111.withmo.core.designsystem.component.TitleLargeText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSearchTextField
import io.github.kei_1111.withmo.core.designsystem.component.WithmoTopAppBar
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.LocalAppList
import io.github.kei_1111.withmo.feature.onboarding.OnboardingAction
import io.github.kei_1111.withmo.feature.onboarding.OnboardingState
import io.github.kei_1111.withmo.feature.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.feature.onboarding.component.OnboardingBottomAppBarPreviousButton
import kotlinx.collections.immutable.toPersistentList

@Suppress("LongMethod")
@Composable
internal fun SelectFavoriteAppContent(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appInfoList = LocalAppList.current

    // LocalAppListから動的に検索結果を計算
    val searchedAppList by remember(appInfoList, state.appSearchQuery) {
        derivedStateOf {
            appInfoList
                .filter { appInfo ->
                    appInfo.label.contains(state.appSearchQuery, ignoreCase = true)
                }
                .sortedBy { it.label }
                .toPersistentList()
        }
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
            )
            if (searchedAppList.isNotEmpty()) {
                FavoriteAppSelector(
                    appList = searchedAppList,
                    favoriteAppInfoList = state.selectedAppList,
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
            favoriteAppInfoList = state.selectedAppList,
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

@Composable
@Preview
private fun SelectFavoriteAppContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SelectFavoriteAppContent(
            state = OnboardingState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun SelectFavoriteAppContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SelectFavoriteAppContent(
            state = OnboardingState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun SelectFavoriteAppContentBottomAppBarLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SelectFavoriteAppContentBottomAppBar(
            state = OnboardingState(),
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun SelectFavoriteAppContentBottomAppBarDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SelectFavoriteAppContentBottomAppBar(
            state = OnboardingState(),
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
