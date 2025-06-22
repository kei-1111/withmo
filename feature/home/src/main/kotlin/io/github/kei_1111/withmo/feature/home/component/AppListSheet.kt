package io.github.kei_1111.withmo.feature.home.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.CenteredMessage
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSearchTextField
import io.github.kei_1111.withmo.core.designsystem.component.theme.BottomSheetShape
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteOrder
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.sortAppList
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.preview.HomeDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.home.preview.HomeLightPreviewEnvironment
import kotlinx.collections.immutable.toPersistentList

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppListSheet(
    appListSheetState: SheetState,
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var appSearchQuery by remember { mutableStateOf("") }
    val searchedAppList by remember(
        appSearchQuery,
        state.appList,
        state.currentUserSettings.sortSettings.sortType,
    ) {
        derivedStateOf {
            val filtered = state.appList.filter { appInfo ->
                appInfo.info.label.contains(appSearchQuery, ignoreCase = true)
            }
            sortAppList(
                sortType = state.currentUserSettings.sortSettings.sortType,
                appList = filtered,
            ).toPersistentList()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onAction(HomeAction.OnAppListSheetSwipeDown) },
        shape = BottomSheetShape,
        sheetState = appListSheetState,
        dragHandle = {},
    ) {
        Surface(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = WindowInsets.safeGestures.asPaddingValues().calculateTopPadding(),
                    )
                    .padding(horizontal = Paddings.Medium),
                verticalArrangement = Arrangement.spacedBy(
                    Paddings.Medium,
                    Alignment.CenterVertically,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WithmoSearchTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = appSearchQuery,
                    onValueChange = { appSearchQuery = it },
                )
                if (searchedAppList.isNotEmpty()) {
                    AppList(
                        appList = searchedAppList,
                        userSettings = state.currentUserSettings,
                        onAppClick = { onAction(HomeAction.OnAppClick(it)) },
                        onAppLongClick = { onAction(HomeAction.OnAppLongClick(it)) },
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    CenteredMessage(
                        message = "アプリが見つかりません",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Suppress("MagicNumber")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun AppListSheetLightPreview() {
    HomeLightPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        AppListSheet(
            appListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
            state = HomeState(
                appList = List(20) {
                    WithmoAppInfo(
                        info = AppInfo(
                            appIcon = appIcon,
                            label = "アプリ $it",
                            packageName = "io.github.kei_1111.withmo.app$it",
                            notification = it % 3 == 0,
                        ),
                        favoriteOrder = FavoriteOrder.NotFavorite,
                        position = Offset.Unspecified,
                    )
                }.toPersistentList(),
            ),
            onAction = {},
        )
    }
}

@Suppress("MagicNumber")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun AppListSheetDarkPreview() {
    HomeDarkPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        AppListSheet(
            appListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
            state = HomeState(
                appList = List(20) {
                    WithmoAppInfo(
                        info = AppInfo(
                            appIcon = appIcon,
                            label = "アプリ $it",
                            packageName = "io.github.kei_1111.withmo.app$it",
                            notification = it % 3 == 0,
                        ),
                        favoriteOrder = FavoriteOrder.NotFavorite,
                        position = Offset.Unspecified,
                    )
                }.toPersistentList(),
            ),
            onAction = {},
        )
    }
}
