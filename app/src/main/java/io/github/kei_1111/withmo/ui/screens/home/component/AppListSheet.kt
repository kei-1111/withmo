package io.github.kei_1111.withmo.ui.screens.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.user_settings.toShape
import io.github.kei_1111.withmo.ui.component.AppItem
import io.github.kei_1111.withmo.ui.component.CenteredMessage
import io.github.kei_1111.withmo.ui.component.LabelMediumText
import io.github.kei_1111.withmo.ui.component.WithmoSearchTextField
import io.github.kei_1111.withmo.ui.screens.home.HomeUiEvent
import io.github.kei_1111.withmo.ui.screens.home.HomeUiState
import io.github.kei_1111.withmo.ui.theme.BottomSheetShape
import io.github.kei_1111.withmo.ui.theme.DesignConstants
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppListSheet(
    appList: ImmutableList<AppInfo>,
    appListSheetState: SheetState,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var resultAppList by remember { mutableStateOf(appList) }

    LaunchedEffect(appList) {
        resultAppList = appList.filter { appInfo ->
            appInfo.label.contains(uiState.appSearchQuery, ignoreCase = true)
        }.toPersistentList()
    }

    ModalBottomSheet(
        onDismissRequest = { onEvent(HomeUiEvent.HideAppListBottomSheet) },
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
                    onValueChange = { onEvent(HomeUiEvent.OnValueChangeAppSearchQuery(it)) },
                    action = {
                        resultAppList = appList.filter { appInfo ->
                            appInfo.label.contains(uiState.appSearchQuery, ignoreCase = true)
                        }.toPersistentList()
                    },
                )
                if (resultAppList.isNotEmpty()) {
                    AppList(
                        appList = resultAppList,
                        appIconShape = uiState.currentUserSettings.appIconSettings.appIconShape.toShape(
                            uiState.currentUserSettings.appIconSettings.roundedCornerPercent,
                        ),
                        onEvent = onEvent,
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

@Composable
private fun AppList(
    appList: ImmutableList<AppInfo>,
    appIconShape: Shape,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val launchableAppList = appList
        .filter { it.packageName != context.packageName }
        .toPersistentList()
    val settingApp = appList
        .filter { it.packageName == context.packageName }
        .toPersistentList()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        if (launchableAppList.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
            ) {
                LabelMediumText(
                    text = "アプリ一覧",
                )
                CustomAppInfoGridLayout(
                    items = launchableAppList,
                    columns = DesignConstants.AppListGridColums,
                    appIconShape = appIconShape,
                    onClick = { onEvent(HomeUiEvent.StartApp(it)) },
                    onLongClick = { onEvent(HomeUiEvent.DeleteApp(it)) },
                )
            }
        }
        if (settingApp.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
            ) {
                LabelMediumText(
                    text = "カスタマイズ",
                )
                CustomAppInfoGridLayout(
                    items = settingApp,
                    columns = DesignConstants.AppListGridColums,
                    appIconShape = appIconShape,
                    onClick = { onEvent(HomeUiEvent.StartApp(it)) },
                    onLongClick = { onEvent(HomeUiEvent.DeleteApp(it)) },
                )
            }
        }
    }
}

@Composable
private fun CustomAppInfoGridLayout(
    items: ImmutableList<AppInfo>,
    columns: Int,
    appIconShape: Shape,
    onClick: (AppInfo) -> Unit,
    onLongClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = Paddings.Large,
    horizontalSpacing: Dp = Paddings.Large,
    contentPadding: PaddingValues = PaddingValues(
        bottom = Paddings.ExtraSmall,
    ),
) {
    Column(
        modifier = modifier
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
    ) {
        items.chunked(columns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
            ) {
                rowItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(Weights.Medium),
                        contentAlignment = Alignment.Center,
                    ) {
                        AppItem(
                            appInfo = item,
                            onClick = { onClick(item) },
                            onLongClick = { onLongClick(item) },
                            appIconShape = appIconShape,
                        )
                    }
                }
                if (rowItems.size < columns) {
                    repeat(columns - rowItems.size) {
                        Spacer(modifier = Modifier.weight(Weights.Medium))
                    }
                }
            }
        }
    }
}
