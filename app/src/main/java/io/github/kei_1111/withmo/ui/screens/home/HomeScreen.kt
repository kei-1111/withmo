package io.github.kei_1111.withmo.ui.screens.home

import android.app.Activity.RESULT_OK
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.domain.model.user_settings.SortType
import io.github.kei_1111.withmo.domain.model.user_settings.toShape
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.Widget
import io.github.kei_1111.withmo.ui.component.WithmoSettingItemWithSlider
import io.github.kei_1111.withmo.ui.theme.BottomSheetShape
import io.github.kei_1111.withmo.ui.theme.UiConfig
import io.github.kei_1111.withmo.utils.showToast
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Suppress("ModifierMissing", "LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navigateToSettingsScreen: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val appListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val widgetListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val appList by viewModel.appList.collectAsStateWithLifecycle()

    val homeAppList by remember(appList, uiState.currentUserSettings.sortSettings.sortType) {
        derivedStateOf {
            when (uiState.currentUserSettings.sortSettings.sortType) {
                SortType.USE_COUNT -> appList.sortedByDescending { it.useCount }.toPersistentList()
                SortType.ALPHABETICAL -> appList.sortedBy { it.label }.toPersistentList()
            }
        }
    }

    val configureWidgetLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            uiState.pendingWidgetInfo?.let {
                viewModel.addDisplayedWidgetList(
                    WidgetInfo(
                        id = uiState.pendingWidgetId,
                        info = it,
                    ),
                )
            }
        } else {
            viewModel.deleteWidgetId(uiState.pendingWidgetId)
        }
    }

    val bindWidgetLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            uiState.pendingWidgetInfo?.let { widgetInfo ->
                if (widgetInfo.configure != null) {
                    val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
                    intent.component = widgetInfo.configure
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, uiState.pendingWidgetId)
                    configureWidgetLauncher.launch(intent)
                } else {
                    viewModel.addDisplayedWidgetList(
                        WidgetInfo(
                            id = uiState.pendingWidgetId,
                            info = widgetInfo,
                        ),
                    )
                }
            }
        } else {
            viewModel.deleteWidgetId(uiState.pendingWidgetId)
        }
    }

    val openDocumentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        scope.launch {
            if (uri == null) {
                showToast(context, "ファイルが選択されませんでした")
            } else {
                viewModel.setIsModelLoading(true)
                viewModel.deleteCopiedCacheFiles(context)
                val filePath = viewModel.getVrmFilePath(context, uri)
                if (filePath == null) {
                    showToast(context, "ファイルの読み込みに失敗しました")
                    viewModel.setIsModelLoading(false)
                } else {
                    viewModel.saveModelFilePath(ModelFilePath(filePath))
                }
            }
        }
    }
    val isModelChangeWarningFirstShown by viewModel.isModelChangeWarningFirstShown.collectAsStateWithLifecycle()

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is HomeUiEvent.StartApp -> {
                    if (event.appInfo.packageName == context.packageName) {
                        latestNavigateToSettingsScreen()
                    } else {
                        event.appInfo.launch(context = context)
                    }
                }

                is HomeUiEvent.DeleteApp -> {
                    if (event.appInfo.packageName != context.packageName) {
                        event.appInfo.delete(context = context)
                    }
                }

                is HomeUiEvent.SetShowScaleSlider -> {
                    viewModel.setShowScaleSlider(event.isShow)
                }

                is HomeUiEvent.OnOpenDocumentButtonClick -> {
                    if (isModelChangeWarningFirstShown) {
                        openDocumentLauncher.launch(arrayOf("*/*"))
                    } else {
                        viewModel.setIsModelChangeWarningDialogShown(true)
                    }
                }

                is HomeUiEvent.OnModelChangeWarningDialogConfirm -> {
                    viewModel.setIsModelChangeWarningDialogShown(false)
                    viewModel.markModelChangeWarningFirstShown()
                    openDocumentLauncher.launch(arrayOf("*/*"))
                }

                is HomeUiEvent.OnModelChangeWarningDialogDismiss -> {
                    viewModel.setIsModelChangeWarningDialogShown(false)
                }

                is HomeUiEvent.OnValueChangeAppSearchQuery -> {
                    viewModel.setAppSearchQuery(event.query)
                }

                is HomeUiEvent.OpenAppListBottomSheet -> {
                    scope.launch {
                        viewModel.changeIsAppListBottomSheetOpened(true)
                        appListSheetState.show()
                    }
                }

                is HomeUiEvent.HideAppListBottomSheet -> {
                    scope.launch {
                        appListSheetState.hide()
                    }.invokeOnCompletion {
                        if (!appListSheetState.isVisible) {
                            viewModel.changeIsAppListBottomSheetOpened(false)
                        }
                    }
                }

                is HomeUiEvent.OpenWidgetListBottomSheet -> {
                    scope.launch {
                        viewModel.changeIsWidgetListBottomSheetOpened(true)
                        widgetListSheetState.show()
                    }
                }

                is HomeUiEvent.HideWidgetListBottomSheet -> {
                    scope.launch {
                        widgetListSheetState.hide()
                    }.invokeOnCompletion {
                        if (!widgetListSheetState.isVisible) {
                            viewModel.changeIsWidgetListBottomSheetOpened(false)
                        }
                    }
                }

                is HomeUiEvent.OnSelectWidget -> {
                    viewModel.selectWidget(
                        widgetInfo = event.widgetInfo,
                        context = context,
                        configureWidgetLauncher = configureWidgetLauncher,
                        bindWidgetLauncher = bindWidgetLauncher,
                    )
                    viewModel.onEvent(HomeUiEvent.HideWidgetListBottomSheet)
                }

                is HomeUiEvent.EnterEditMode -> {
                    viewModel.changeIsEditMode(true)
                }

                is HomeUiEvent.ExitEditMode -> {
                    viewModel.saveWidgetList()
                    viewModel.changeIsEditMode(false)
                }

                is HomeUiEvent.DeleteWidget -> {
                    viewModel.deleteWidget(event.widgetInfo)
                }

                is HomeUiEvent.ResizeWidget -> {
                    viewModel.changeIsWidgetResizing(true)
                    viewModel.changeResizingWidget(event.widgetInfo)
                    viewModel.deleteWidget(event.widgetInfo)
                }

                is HomeUiEvent.FinishResizeWidget -> {
                    viewModel.changeIsWidgetResizing(false)
                    viewModel.addDisplayedWidgetList(event.widgetInfo)
                    viewModel.changeResizingWidget(null)
                }

                is HomeUiEvent.NavigateToSettingsScreen -> {
                    latestNavigateToSettingsScreen()
                }
            }
        }.launchIn(this)
    }

    HomeScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        homeAppList = homeAppList,
        appListSheetState = appListSheetState,
        widgetListSheetState = widgetListSheetState,
        groupedWidgetInfoMap = viewModel.getGroupedWidgetInfoMap(),
        createWidgetView = viewModel::createWidgetView,
        modifier = Modifier.fillMaxSize(),
    )
}

@Suppress("LongMethod", "MultipleEmitters")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    homeAppList: ImmutableList<AppInfo>,
    appListSheetState: SheetState,
    widgetListSheetState: SheetState,
    groupedWidgetInfoMap: ImmutableMap<String, List<AppWidgetProviderInfo>>,
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    modifier: Modifier = Modifier,
) {
    val topPaddingValue = WindowInsets.safeGestures.asPaddingValues().calculateTopPadding()

    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    if (uiState.isAppListBottomSheetOpened) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(HomeUiEvent.HideAppListBottomSheet) },
            shape = BottomSheetShape,
            sheetState = appListSheetState,
            dragHandle = {},
        ) {
            HomeAppList(
                onClick = { onEvent(HomeUiEvent.StartApp(it)) },
                onLongClick = { onEvent(HomeUiEvent.DeleteApp(it)) },
                appList = homeAppList,
                appIconShape = uiState.currentUserSettings.appIconSettings.appIconShape.toShape(
                    uiState.currentUserSettings.appIconSettings.roundedCornerPercent,
                ),
                appSearchQuery = uiState.appSearchQuery,
                onValueChangeAppSearchQuery = { onEvent(HomeUiEvent.OnValueChangeAppSearchQuery(it)) },
                modifier = modifier,
            )
        }
    }

    if (uiState.isWidgetListBottomSheetOpened) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(HomeUiEvent.HideWidgetListBottomSheet) },
            sheetState = widgetListSheetState,
            modifier = Modifier.padding(
                top = topPaddingValue,
            ),
        ) {
            WidgetList(
                groupedWidgetInfoMap = groupedWidgetInfoMap,
                selectWidget = { onEvent(HomeUiEvent.OnSelectWidget(it)) },
            )
        }
    }

    if (uiState.isWidgetResizing) {
        uiState.resizeWidget?.let { widgetInfo ->
            WidgetResizeBottomSheet(
                widgetInfo = widgetInfo,
                createWidgetView = createWidgetView,
                close = { onEvent(HomeUiEvent.FinishResizeWidget(it)) },
            )
        }
    }

    Box(
        modifier = modifier
            .padding(
                top = topPaddingValue,
                bottom = bottomPaddingValue + UiConfig.MediumPadding,
            ),
    ) {
        HomeScreenContent(
            uiState = uiState,
            onEvent = onEvent,
            createWidgetView = createWidgetView,
            modifier = Modifier.fillMaxSize(),
        )
    }

    if (uiState.isModelChangeWarningDialogShown) {
        ModelChangeWarningDialog(
            onConfirm = { onEvent(HomeUiEvent.OnModelChangeWarningDialogConfirm) },
            onDismiss = { onEvent(HomeUiEvent.OnModelChangeWarningDialogDismiss) },
        )
    }

    if (uiState.isModelLoading) {
        ModelLoading(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetResizeBottomSheet(
    widgetInfo: WidgetInfo,
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    close: (WidgetInfo) -> Unit,
) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val draggedSpaceWidth = screenWidth.dp - UiConfig.MediumPadding - UiConfig.MediumPadding
    val draggedSpaceHeight = screenHeight.dp - UiConfig.MediumPadding - UiConfig.MediumPadding
    val minDraggedSpaceDimension = min(draggedSpaceWidth, draggedSpaceHeight)

    var widgetWidth by remember { mutableFloatStateOf(widgetInfo.width.toFloat()) }
    var widgetHeight by remember { mutableFloatStateOf(widgetInfo.height.toFloat()) }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            close(
                widgetInfo.copy(
                    width = widgetWidth.roundToInt(),
                    height = widgetHeight.roundToInt(),
                ),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Widget(
                widgetInfo = widgetInfo.copy(
                    width = widgetWidth.roundToInt(),
                    height = widgetHeight.roundToInt(),
                ),
                createWidgetView = createWidgetView,
            )
            Column(
                modifier = Modifier.padding(UiConfig.MediumPadding),
                verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
            ) {
                WithmoSettingItemWithSlider(
                    title = "Widget 幅",
                    value = widgetWidth,
                    onValueChange = { widgetWidth = it },
                    valueRange = (minDraggedSpaceDimension / 3f).value..minDraggedSpaceDimension.value,
                    modifier = Modifier.fillMaxWidth(),
                    steps = 1,
                )
                WithmoSettingItemWithSlider(
                    title = "Widget 高さ",
                    value = widgetHeight,
                    onValueChange = { widgetHeight = it },
                    valueRange = (minDraggedSpaceDimension / 3f).value..minDraggedSpaceDimension.value,
                    modifier = Modifier.fillMaxWidth(),
                    steps = 1,
                )
            }
        }
    }
}

@Composable
private fun ModelLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                    }
                }
            }
            .padding(UiConfig.MediumPadding),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .height(UiConfig.ModelLoadingHeight)
                .width(UiConfig.ModelLoadingWidth),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = UiConfig.ShadowElevation,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement
                    .spacedBy(UiConfig.LargePadding, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BodyMediumText("モデルの読込中")
                CircularProgressIndicator()
            }
        }
    }
}
