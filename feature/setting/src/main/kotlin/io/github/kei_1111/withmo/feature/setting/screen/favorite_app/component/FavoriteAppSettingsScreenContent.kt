package io.github.kei_1111.withmo.feature.setting.screen.favorite_app.component

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.CenteredMessage
import io.github.kei_1111.withmo.core.designsystem.component.FavoriteAppListRow
import io.github.kei_1111.withmo.core.designsystem.component.FavoriteAppSelector
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSearchTextField
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.core.ui.LocalAppList
import io.github.kei_1111.withmo.feature.setting.screen.favorite_app.FavoriteAppSettingsAction
import io.github.kei_1111.withmo.feature.setting.screen.favorite_app.FavoriteAppSettingsState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Suppress("LongMethod")
@Composable
internal fun FavoriteAppSettingsScreenContent(
    state: FavoriteAppSettingsState.Stable,
    onAction: (FavoriteAppSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appIconShape = state.appIconSettings.appIconShape.toShape(
        state.appIconSettings.roundedCornerPercent,
    )
    val appList = LocalAppList.current
    val searchedAppList by remember(appList, state.appSearchQuery) {
        derivedStateOf {
            appList
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
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WithmoSearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.appSearchQuery,
                onValueChange = { onAction(FavoriteAppSettingsAction.OnAppSearchQueryChange(it)) },
            )
            if (searchedAppList.isNotEmpty()) {
                FavoriteAppSelector(
                    appList = searchedAppList,
                    favoriteAppInfoList = state.favoriteAppList,
                    addSelectedAppList = { onAction(FavoriteAppSettingsAction.OnAllAppListAppClick(it)) },
                    removeSelectedAppList = { onAction(FavoriteAppSettingsAction.OnFavoriteAppListAppClick(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    appIconShape = appIconShape,
                )
            } else {
                CenteredMessage(
                    message = "アプリが見つかりません",
                    modifier = Modifier.weight(1f),
                )
            }
        }
        FavoriteAppListRow(
            favoriteAppInfoList = state.favoriteAppList,
            removeSelectedAppList = { onAction(FavoriteAppSettingsAction.OnFavoriteAppListAppClick(it)) },
            appIconShape = appIconShape,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Suppress("MagicNumber")
@Composable
@Preview
private fun FavoriteAppSettingsScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        FavoriteAppSettingsScreenContent(
            state = FavoriteAppSettingsState.Stable(
                favoriteAppList = List(3) {
                    FavoriteAppInfo(
                        info = AppInfo(
                            appIcon = appIcon,
                            label = "アプリ $it",
                            packageName = "io.github.kei_1111.withmo.app$it",
                            notification = it % 2 == 0,
                        ),
                        favoriteOrder = it,
                    )
                }.toPersistentList(),
                appIconSettings = AppIconSettings(),
                appSearchQuery = "search",
                isSaveButtonEnabled = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Suppress("MagicNumber")
@Composable
@Preview
private fun FavoriteAppSettingsScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        FavoriteAppSettingsScreenContent(
            state = FavoriteAppSettingsState.Stable(
                favoriteAppList = persistentListOf(),
                appIconSettings = AppIconSettings(),
                appSearchQuery = "",
                isSaveButtonEnabled = false,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
