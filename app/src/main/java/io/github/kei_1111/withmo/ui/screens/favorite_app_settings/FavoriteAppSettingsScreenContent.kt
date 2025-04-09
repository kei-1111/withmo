package io.github.kei_1111.withmo.ui.screens.favorite_app_settings

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
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun FavoriteAppSettingsScreenContent(
    appList: ImmutableList<AppInfo>,
    uiState: FavoriteAppSettingsUiState,
    onEvent: (FavoriteAppSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var resultAppList by remember { mutableStateOf(appList) }

    fun filterAppList(query: String) {
        resultAppList =
            appList.filter { it.label.contains(query, ignoreCase = true) }.toPersistentList()
    }

    val appIconShape = uiState.appIconSettings.appIconShape.toShape(
        uiState.appIconSettings.roundedCornerPercent,
    )

    LaunchedEffect(appList) {
        filterAppList(uiState.appSearchQuery)
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
                value = uiState.appSearchQuery,
                onValueChange = { onEvent(FavoriteAppSettingsUiEvent.OnValueChangeAppSearchQuery(it)) },
                action = { filterAppList(uiState.appSearchQuery) },
            )
            if (resultAppList.isNotEmpty()) {
                FavoriteAppSelector(
                    appList = resultAppList,
                    favoriteAppList = uiState.favoriteAppList,
                    addSelectedAppList = { onEvent(FavoriteAppSettingsUiEvent.AddFavoriteAppList(it)) },
                    removeSelectedAppList = {
                        onEvent(FavoriteAppSettingsUiEvent.RemoveFavoriteAppList(it))
                    },
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
            favoriteAppList = uiState.favoriteAppList,
            removeSelectedAppList = { onEvent(FavoriteAppSettingsUiEvent.RemoveFavoriteAppList(it)) },
            appIconShape = appIconShape,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
