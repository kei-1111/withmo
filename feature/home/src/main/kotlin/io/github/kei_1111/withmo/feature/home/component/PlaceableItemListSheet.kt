package io.github.kei_1111.withmo.feature.home.component

import android.appwidget.AppWidgetProviderInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.CenteredMessage
import io.github.kei_1111.withmo.core.designsystem.component.LabelMediumText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSearchTextField
import io.github.kei_1111.withmo.core.designsystem.component.theme.BottomSheetShape
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.IconSizes
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.sortAppList
import io.github.kei_1111.withmo.core.ui.LocalAppList
import io.github.kei_1111.withmo.core.ui.LocalAppWidgetManager
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable
import io.github.kei_1111.withmo.core.util.WidgetUtils
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeScreenDimensions
import io.github.kei_1111.withmo.feature.home.HomeState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.launch

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun PlaceableItemListSheet(
    placeableItemListSheetState: SheetState,
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appList = LocalAppList.current
    var appSearchQuery by remember { mutableStateOf("") }
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

    val pagerState = rememberPagerState(pageCount = { PlaceableItemTab.entries.size })
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onAction(HomeAction.OnPlaceableItemListSheetSwipeDown) },
        shape = BottomSheetShape,
        sheetState = placeableItemListSheetState,
        dragHandle = {},
        contentWindowInsets = { WindowInsets(bottom = 0) },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
            ) {
                PlaceableItemTab.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        modifier = Modifier.height(HomeScreenDimensions.PlaceableItemTabHeight),
                        text = {
                            BodyMediumText(
                                text = when (tab) {
                                    PlaceableItemTab.Widget -> "ウィジェット"
                                    PlaceableItemTab.App -> "アプリ"
                                },
                            )
                        },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (PlaceableItemTab.entries[page]) {
                    PlaceableItemTab.Widget -> {
                        WidgetTabContent(
                            onAction = onAction,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    PlaceableItemTab.App -> {
                        AppTabContent(
                            appSearchQuery = appSearchQuery,
                            onAppSearchQueryChange = { appSearchQuery = it },
                            searchedAppList = searchedAppList,
                            state = state,
                            onAction = onAction,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

enum class PlaceableItemTab {
    Widget,
    App,
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun WidgetTabContent(
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appWidgetManager = LocalAppWidgetManager.current
    val groupedWidgetInfoMaps = remember(appWidgetManager.installedProviders) {
        appWidgetManager.installedProviders.groupBy { it.provider.packageName }.toPersistentMap()
    }

    WidgetList(
        groupedWidgetInfoMaps = groupedWidgetInfoMaps,
        selectWidget = { onAction(HomeAction.OnPlaceableItemListSheetWidgetClick(it)) },
        modifier = modifier,
    )
}

@Suppress("LongMethod")
@Composable
private fun AppTabContent(
    appSearchQuery: String,
    onAppSearchQueryChange: (String) -> Unit,
    searchedAppList: ImmutableList<AppInfo>,
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WithmoSearchTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Paddings.Medium),
            value = appSearchQuery,
            onValueChange = onAppSearchQueryChange,
        )
        if (searchedAppList.isNotEmpty()) {
            AppList(
                appList = searchedAppList,
                userSettings = state.currentUserSettings,
                onAppClick = { onAction(HomeAction.OnPlaceableItemListSheetAppClick(it)) },
                onAppLongClick = {},
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun WidgetList(
    groupedWidgetInfoMaps: ImmutableMap<String, List<AppWidgetProviderInfo>>,
    selectWidget: (AppWidgetProviderInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val nestedScrollConnection = rememberNestedScrollInteropConnection()

    LazyColumn(
        modifier = modifier.nestedScroll(nestedScrollConnection),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
        contentPadding = PaddingValues(
            start = Paddings.Medium,
            end = Paddings.Medium,
            top = Paddings.Medium,
            bottom = Paddings.Medium + WindowInsets.navigationBars.asPaddingValues()
                .calculateBottomPadding(),
        ),
    ) {
        groupedWidgetInfoMaps.forEach { (packageName, widgetInfoList) ->
            item {
                WidgetContainer(
                    packageName = packageName,
                    widgetInfoList = widgetInfoList.toPersistentList(),
                    selectWidget = selectWidget,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Suppress("LongMethod")
@Composable
private fun WidgetContainer(
    packageName: String,
    widgetInfoList: ImmutableList<AppWidgetProviderInfo>,
    selectWidget: (AppWidgetProviderInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val appIcon = remember { WidgetUtils.loadAppIcon(context, packageName) }
    val appLabel = remember { WidgetUtils.loadAppLabel(context, packageName) }

    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.animateContentSize(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .height(CommonDimensions.SettingItemHeight)
                    .fillMaxWidth()
                    .safeClickable { expanded = !expanded }
                    .padding(horizontal = Paddings.Medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                appIcon?.let {
                    Image(
                        painter = rememberDrawablePainter(drawable = appIcon),
                        contentDescription = null,
                        modifier = Modifier.size(IconSizes.Medium),
                    )
                }
                Spacer(
                    modifier = Modifier.width(Paddings.ExtraSmall),
                )
                appLabel?.let {
                    BodyMediumText(
                        text = appLabel,
                        modifier = Modifier.weight(Weights.Medium),
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = if (expanded) "Close" else "Open",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .padding(vertical = Paddings.Medium)
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    widgetInfoList.forEach { widgetInfo ->
                        WidgetItem(
                            widgetInfo = widgetInfo,
                            selectWidget = selectWidget,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

private const val WidgetDescriptionMaxLines = 3

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun WidgetItem(
    widgetInfo: AppWidgetProviderInfo,
    selectWidget: (AppWidgetProviderInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val widgetLabel = widgetInfo.loadLabel(context.packageManager)
    val widgetDescription = widgetInfo.loadDescription(context)
    val previewDrawable = remember { WidgetUtils.loadWidgetPreviewImage(context, widgetInfo) }
    val previewLayout = remember { WidgetUtils.loadWidgetPreviewLayout(context, widgetInfo) }

    Column(
        modifier = modifier
            .safeClickable { selectWidget(widgetInfo) }
            .padding(horizontal = Paddings.Medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (previewDrawable != null) {
            Image(
                painter = rememberDrawablePainter(drawable = previewDrawable),
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(
                        maxWidth = HomeScreenDimensions.WidgetPreviewSize,
                        maxHeight = HomeScreenDimensions.WidgetPreviewSize,
                    ),
            )
        } else {
            previewLayout?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .sizeIn(
                            maxWidth = HomeScreenDimensions.WidgetPreviewSize,
                            maxHeight = HomeScreenDimensions.WidgetPreviewSize,
                        ),
                )
            }
        }
        Spacer(
            modifier = Modifier.height(Paddings.Tiny),
        )
        widgetLabel?.let {
            BodyMediumText(text = it)
        }
        widgetDescription?.let {
            LabelMediumText(
                text = it.toString(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled),
                maxLines = WidgetDescriptionMaxLines,
            )
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun PlaceableItemListSheetAppTabLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(
                    context,
                    io.github.kei_1111.withmo.core.designsystem.R.drawable.withmo_icon_wide,
                )!!,
                backgroundIcon = null,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TabRow(
                selectedTabIndex = PlaceableItemTab.App.ordinal,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Tab(
                    selected = false,
                    onClick = {},
                    modifier = Modifier.height(HomeScreenDimensions.PlaceableItemTabHeight),
                    text = { BodyMediumText(text = "ウィジェット") },
                )
                Tab(
                    selected = true,
                    onClick = {},
                    modifier = Modifier.height(HomeScreenDimensions.PlaceableItemTabHeight),
                    text = { BodyMediumText(text = "アプリ") },
                )
            }

            AppTabContent(
                appSearchQuery = "",
                onAppSearchQueryChange = {},
                searchedAppList = List(20) {
                    AppInfo(
                        appIcon = appIcon,
                        label = "アプリ $it",
                        packageName = "io.github.kei_1111.withmo.app$it",
                        notification = it % 3 == 0,
                    )
                }.toPersistentList(),
                state = HomeState(),
                onAction = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun PlaceableItemListSheetAppTabDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(
                    context,
                    io.github.kei_1111.withmo.core.designsystem.R.drawable.withmo_icon_wide,
                )!!,
                backgroundIcon = null,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TabRow(
                selectedTabIndex = PlaceableItemTab.App.ordinal,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Tab(
                    selected = false,
                    onClick = {},
                    modifier = Modifier.height(HomeScreenDimensions.PlaceableItemTabHeight),
                    text = { BodyMediumText(text = "ウィジェット") },
                )
                Tab(
                    selected = true,
                    onClick = {},
                    modifier = Modifier.height(HomeScreenDimensions.PlaceableItemTabHeight),
                    text = { BodyMediumText(text = "アプリ") },
                )
            }

            AppTabContent(
                appSearchQuery = "",
                onAppSearchQueryChange = {},
                searchedAppList = List(20) {
                    AppInfo(
                        appIcon = appIcon,
                        label = "アプリ $it",
                        packageName = "io.github.kei_1111.withmo.app$it",
                        notification = it % 3 == 0,
                    )
                }.toPersistentList(),
                state = HomeState(),
                onAction = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun AppTabContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(
                    context,
                    io.github.kei_1111.withmo.core.designsystem.R.drawable.withmo_icon_wide,
                )!!,
                backgroundIcon = null,
            )
        }

        AppTabContent(
            appSearchQuery = "",
            onAppSearchQueryChange = {},
            searchedAppList = List(15) {
                AppInfo(
                    appIcon = appIcon,
                    label = "アプリ $it",
                    packageName = "io.github.kei_1111.withmo.app$it",
                    notification = it % 3 == 0,
                )
            }.toPersistentList(),
            state = HomeState(),
            onAction = {},
        )
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun AppTabContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(
                    context,
                    io.github.kei_1111.withmo.core.designsystem.R.drawable.withmo_icon_wide,
                )!!,
                backgroundIcon = null,
            )
        }

        AppTabContent(
            appSearchQuery = "",
            onAppSearchQueryChange = {},
            searchedAppList = List(15) {
                AppInfo(
                    appIcon = appIcon,
                    label = "アプリ $it",
                    packageName = "io.github.kei_1111.withmo.app$it",
                    notification = it % 3 == 0,
                )
            }.toPersistentList(),
            state = HomeState(),
            onAction = {},
        )
    }
}

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
private fun WidgetContainerLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WidgetContainer(
            packageName = "com.android.clock",
            widgetInfoList = listOf<AppWidgetProviderInfo>().toPersistentList(),
            selectWidget = {},
        )
    }
}

@Suppress("MagicNumber")
@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
private fun WidgetContainerDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WidgetContainer(
            packageName = "com.android.clock",
            widgetInfoList = listOf<AppWidgetProviderInfo>().toPersistentList(),
            selectWidget = {},
        )
    }
}
