package io.github.kei_1111.withmo.feature.setting.favorite_app.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.CenteredMessage
import io.github.kei_1111.withmo.core.designsystem.component.FavoriteAppListRow
import io.github.kei_1111.withmo.core.designsystem.component.FavoriteAppSelector
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSearchTextField
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.feature.setting.favorite_app.FavoriteAppSettingsAction
import io.github.kei_1111.withmo.feature.setting.favorite_app.FavoriteAppSettingsState
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun FavoriteAppSettingsScreenContent(
    state: FavoriteAppSettingsState,
    onAction: (FavoriteAppSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appIconShape = state.appIconSettings.appIconShape.toShape(
        state.appIconSettings.roundedCornerPercent,
    )

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
            )
            if (state.searchedAppList.isNotEmpty()) {
                FavoriteAppSelector(
                    appList = state.searchedAppList,
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

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun FavoriteAppSettingsScreenContentLightPreview() {
    SettingLightPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        FavoriteAppSettingsScreenContent(
            state = FavoriteAppSettingsState(
                searchedAppList = List(6) {
                    AppInfo(
                        appIcon = appIcon,
                        label = "アプリ $it",
                        packageName = "io.github.kei_1111.withmo.app$it",
                        notification = it % 2 == 0,
                    )
                }.toPersistentList(),
                favoriteAppList = List(3) {
                    AppInfo(
                        appIcon = appIcon,
                        label = "お気に入り $it",
                        packageName = "io.github.kei_1111.withmo.favorite$it",
                        notification = false,
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun FavoriteAppSettingsScreenContentDarkPreview() {
    SettingDarkPreviewEnvironment {
        FavoriteAppSettingsScreenContent(
            state = FavoriteAppSettingsState(
                searchedAppList = persistentListOf(),
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
