package io.github.kei_1111.withmo.feature.home.component

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
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.CenteredMessage
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSearchTextField
import io.github.kei_1111.withmo.core.designsystem.component.theme.BottomSheetShape
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.sortAppList
import io.github.kei_1111.withmo.core.ui.LocalAppList
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeState
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
    val appList = LocalAppList.current
    val searchedAppList by remember(
        appSearchQuery,
        appList,
        state.currentUserSettings.sortSettings.sortType,
    ) {
        derivedStateOf {
            val filtered = appList.filter { appInfo ->
                appInfo.label.contains(appSearchQuery, ignoreCase = true)
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
@Preview
@Composable
private fun AppListSheetLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        AppListSheet(
            appListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
            state = HomeState(),
            onAction = {},
        )
    }
}

@Suppress("MagicNumber")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AppListSheetDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        AppListSheet(
            appListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
            state = HomeState(),
            onAction = {},
        )
    }
}
