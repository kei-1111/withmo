package io.github.kei_1111.withmo.feature.onboarding.select_favorite_app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.CenteredMessage
import io.github.kei_1111.withmo.core.designsystem.component.FavoriteAppListRow
import io.github.kei_1111.withmo.core.designsystem.component.FavoriteAppSelector
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSearchTextField
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.LocalAppList
import io.github.kei_1111.withmo.feature.onboarding.select_favorite_app.SelectFavoriteAppAction
import io.github.kei_1111.withmo.feature.onboarding.select_favorite_app.SelectFavoriteAppState
import kotlinx.collections.immutable.toPersistentList

@Suppress("LongMethod")
@Composable
internal fun SelectFavoriteAppScreenContent(
    state: SelectFavoriteAppState.Stable,
    onAction: (SelectFavoriteAppAction) -> Unit,
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

    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WithmoSearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.appSearchQuery,
                onValueChange = { onAction(SelectFavoriteAppAction.OnAppSearchQueryChange(it)) },
            )
            if (searchedAppList.isNotEmpty()) {
                FavoriteAppSelector(
                    appList = searchedAppList,
                    favoriteAppInfoList = state.selectedAppList,
                    addSelectedAppList = { onAction(SelectFavoriteAppAction.OnAllAppListAppClick(it)) },
                    removeSelectedAppList = { onAction(SelectFavoriteAppAction.OnFavoriteAppListAppClick(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            } else {
                CenteredMessage(
                    message = "アプリが見つかりません",
                    modifier = Modifier.weight(1f),
                )
            }
        }
        FavoriteAppListRow(
            favoriteAppInfoList = state.selectedAppList,
            removeSelectedAppList = { onAction(SelectFavoriteAppAction.OnFavoriteAppListAppClick(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun SelectFavoriteAppScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SelectFavoriteAppScreenContent(
            state = SelectFavoriteAppState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun SelectFavoriteAppScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SelectFavoriteAppScreenContent(
            state = SelectFavoriteAppState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
