package io.github.kei_1111.withmo.feature.home.screens.component

import android.appwidget.AppWidgetProviderInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.App
import io.github.kei_1111.withmo.core.designsystem.component.AppList
import io.github.kei_1111.withmo.core.designsystem.component.CenteredMessage
import io.github.kei_1111.withmo.core.designsystem.component.WithmoSearchTextField
import io.github.kei_1111.withmo.core.designsystem.component.theme.BottomSheetShape
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.sortAppList
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.core.ui.LocalAppList
import io.github.kei_1111.withmo.core.ui.LocalAppWidgetManager
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable
import io.github.kei_1111.withmo.core.util.WidgetUtils
import io.github.kei_1111.withmo.feature.home.screens.HomeAction
import io.github.kei_1111.withmo.feature.home.screens.HomeState
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
    state: HomeState.Stable,
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
        containerColor = WithmoTheme.colorScheme.surface,
        dragHandle = {},
        contentWindowInsets = { WindowInsets(bottom = 0) },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = WithmoTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = WithmoTheme.colorScheme.primary,
                    )
                },
            ) {
                PlaceableItemTab.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        modifier = Modifier.height(60.dp),
                        text = {
                            Text(
                                text = when (tab) {
                                    PlaceableItemTab.WIDGET -> "ウィジェット"
                                    PlaceableItemTab.APP -> "アプリ"
                                },
                                color = WithmoTheme.colorScheme.onSurface,
                                style = WithmoTheme.typography.bodyMedium,
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
                    PlaceableItemTab.WIDGET -> {
                        WidgetTabContent(
                            onAction = onAction,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    PlaceableItemTab.APP -> {
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
    WIDGET,
    APP,
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
    state: HomeState.Stable,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val notificationSettings = state.currentUserSettings.notificationSettings
    val appIconSettings = state.currentUserSettings.appIconSettings
    val sideButtonSettings = state.currentUserSettings.sideButtonSettings

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WithmoSearchTextField(
            modifier = Modifier.fillMaxWidth(),
            value = appSearchQuery,
            onValueChange = onAppSearchQueryChange,
        )
        if (searchedAppList.isNotEmpty()) {
            AppList(
                appList = searchedAppList,
                appContent = {
                    App(
                        appInfo = it,
                        isNotificationBadgeShown = notificationSettings.isNotificationBadgeEnabled,
                        onClick = { onAction(HomeAction.OnPlaceableItemListSheetAppClick(it)) },
                        appIconShape = appIconSettings.appIconShape.toShape(
                            appIconSettings.roundedCornerPercent,
                        ),
                    )
                },
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 4.dp,
                    bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                ),
                isShowCustomizeApp = !sideButtonSettings.isNavigateSettingsButtonShown,
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
        ),
    ) {
        groupedWidgetInfoMaps.forEach { (packageName, widgetInfoList) ->
            item {
                WidgetPreviewContainer(
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
private fun WidgetPreviewContainer(
    packageName: String,
    widgetInfoList: ImmutableList<AppWidgetProviderInfo>,
    selectWidget: (AppWidgetProviderInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val appIcon = remember { WidgetUtils.loadAppIcon(context, packageName) }
    val appLabel = remember { WidgetUtils.loadAppLabel(context, packageName) }

    var expanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrow_rotation",
    )

    Surface(
        modifier = modifier.animateContentSize(),
        shape = WithmoTheme.shapes.medium,
        color = WithmoTheme.colorScheme.surfaceContainer,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .safeClickable { expanded = !expanded }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                appIcon?.let {
                    Image(
                        painter = rememberDrawablePainter(drawable = appIcon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                appLabel?.let {
                    Text(
                        text = appLabel,
                        color = WithmoTheme.colorScheme.onSurface,
                        style = WithmoTheme.typography.bodyMedium,
                    )
                }
                Spacer(
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = if (expanded) "Close" else "Open",
                    tint = WithmoTheme.colorScheme.onSurface,
                    modifier = Modifier.rotate(rotationAngle),
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    widgetInfoList.forEach { widgetInfo ->
                        WidgetPreviewItem(
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun WidgetPreviewItem(
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
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (previewDrawable != null) {
            Image(
                painter = rememberDrawablePainter(drawable = previewDrawable),
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(
                        maxWidth = 150.dp,
                        maxHeight = 150.dp,
                    ),
            )
        } else {
            previewLayout?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .sizeIn(
                            maxWidth = 150.dp,
                            maxHeight = 150.dp,
                        ),
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        widgetLabel?.let {
            Text(
                text = it,
                color = WithmoTheme.colorScheme.onSurface,
                style = WithmoTheme.typography.bodyMedium,
            )
        }
        widgetDescription?.let {
            Text(
                text = it.toString(),
                color = WithmoTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                maxLines = 3,
                style = WithmoTheme.typography.labelMedium,
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
                    R.drawable.withmo_icon_wide,
                )!!,
                backgroundIcon = null,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TabRow(
                selectedTabIndex = PlaceableItemTab.APP.ordinal,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Tab(
                    selected = false,
                    onClick = {},
                    modifier = Modifier.height(60.dp),
                    text = {
                        Text(
                            text = "ウィジェット",
                            color = WithmoTheme.colorScheme.onSurface,
                            style = WithmoTheme.typography.bodyMedium,
                        )
                    },
                )
                Tab(
                    selected = true,
                    onClick = {},
                    modifier = Modifier.height(60.dp),
                    text = {
                        Text(
                            text = "アプリ",
                            color = WithmoTheme.colorScheme.onSurface,
                            style = WithmoTheme.typography.bodyMedium,
                        )
                    },
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
                state = HomeState.Stable(),
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
                    R.drawable.withmo_icon_wide,
                )!!,
                backgroundIcon = null,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TabRow(
                selectedTabIndex = PlaceableItemTab.APP.ordinal,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Tab(
                    selected = false,
                    onClick = {},
                    modifier = Modifier.height(60.dp),
                    text = {
                        Text(
                            text = "ウィジェット",
                            color = WithmoTheme.colorScheme.onSurface,
                            style = WithmoTheme.typography.bodyMedium,
                        )
                    },
                )
                Tab(
                    selected = true,
                    onClick = {},
                    modifier = Modifier.height(60.dp),
                    text = {
                        Text(
                            text = "アプリ",
                            color = WithmoTheme.colorScheme.onSurface,
                            style = WithmoTheme.typography.bodyMedium,
                        )
                    },
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
                state = HomeState.Stable(),
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
                    R.drawable.withmo_icon_wide,
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
            state = HomeState.Stable(),
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
                    R.drawable.withmo_icon_wide,
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
            state = HomeState.Stable(),
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
        WidgetPreviewContainer(
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
        WidgetPreviewContainer(
            packageName = "com.android.clock",
            widgetInfoList = listOf<AppWidgetProviderInfo>().toPersistentList(),
            selectWidget = {},
        )
    }
}
