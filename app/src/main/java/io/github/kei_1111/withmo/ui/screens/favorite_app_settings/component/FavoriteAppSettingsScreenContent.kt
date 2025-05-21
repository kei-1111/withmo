package io.github.kei_1111.withmo.ui.screens.favorite_app_settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import io.github.kei_1111.withmo.domain.model.user_settings.toShape
import io.github.kei_1111.withmo.ui.component.CenteredMessage
import io.github.kei_1111.withmo.ui.component.WithmoSearchTextField
import io.github.kei_1111.withmo.ui.component.favorite_settings.FavoriteAppListRow
import io.github.kei_1111.withmo.ui.component.favorite_settings.FavoriteAppSelector
import io.github.kei_1111.withmo.ui.screens.favorite_app_settings.FavoriteAppSettingsAction
import io.github.kei_1111.withmo.ui.screens.favorite_app_settings.FavoriteAppSettingsState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun FavoriteAppSettingsScreenContent(
    appList: ImmutableList<AppInfo>,
    state: FavoriteAppSettingsState,
    onAction: (FavoriteAppSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var resultAppList by remember { mutableStateOf(appList) }

    fun filterAppList(query: String) {
        resultAppList =
            appList.filter { it.label.contains(query, ignoreCase = true) }.toPersistentList()
    }

    val appIconShape = state.appIconSettings.appIconShape.toShape(
        state.appIconSettings.roundedCornerPercent,
    )

    LaunchedEffect(appList) {
        filterAppList(state.appSearchQuery)
    }

    Column(
        modifier = modifier,
    ) {
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
                onValueChange = { onAction(FavoriteAppSettingsAction.OnAppSearchQueryChange(it)) },
                action = { filterAppList(state.appSearchQuery) },
            )
            if (resultAppList.isNotEmpty()) {
                FavoriteAppSelector(
                    appList = resultAppList,
                    favoriteAppList = state.favoriteAppList,
                    addSelectedAppList = { onAction(FavoriteAppSettingsAction.OnAllAppListAppClick(it)) },
                    removeSelectedAppList = { onAction(FavoriteAppSettingsAction.OnFavoriteAppListAppClick(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(Weights.Medium),
                    appIconShape = appIconShape,
                )
            } else {
                CenteredMessage(
                    message = "アプリが見つかりません",
                    modifier = Modifier.weight(Weights.Medium),
                )
            }
        }
        FavoriteAppListRow(
            favoriteAppList = state.favoriteAppList,
            removeSelectedAppList = { onAction(FavoriteAppSettingsAction.OnFavoriteAppListAppClick(it)) },
            appIconShape = appIconShape,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
