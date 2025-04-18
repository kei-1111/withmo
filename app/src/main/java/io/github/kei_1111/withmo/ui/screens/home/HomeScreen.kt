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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.unity3d.player.UnityPlayer.UnitySendMessage
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.domain.model.user_settings.SortType
import io.github.kei_1111.withmo.ui.screens.home.component.AppListSheet
import io.github.kei_1111.withmo.ui.screens.home.component.HomeScreenContent
import io.github.kei_1111.withmo.ui.screens.home.component.ModelChangeWarningDialog
import io.github.kei_1111.withmo.ui.screens.home.component.ModelLoading
import io.github.kei_1111.withmo.ui.screens.home.component.WidgetListSheet
import io.github.kei_1111.withmo.ui.screens.home.component.WidgetResizeBottomSheet
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.utils.AppUtils
import io.github.kei_1111.withmo.utils.FileUtils
import io.github.kei_1111.withmo.utils.showToast
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
                    if (filePath == uiState.currentUserSettings.modelFilePath.path) {
                        showToast(context, "同じファイルが選択されています")
                        viewModel.setIsModelLoading(false)
                    } else {
                        viewModel.saveModelFilePath(ModelFilePath(filePath))
                    }
                }
            }
        }
    }
    val isModelChangeWarningFirstShown by viewModel.isModelChangeWarningFirstShown.collectAsStateWithLifecycle()

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is HomeUiEvent.OnAppClick -> {
                    if (event.appInfo.packageName == context.packageName) {
                        latestNavigateToSettingsScreen()
                    } else {
                        event.appInfo.launch(context = context)
                    }
                }

                is HomeUiEvent.OnAppLongClick -> {
                    val isSystemApp = AppUtils.isSystemApp(context, event.appInfo.packageName)
                    val isProtectedApp = AppUtils.isProtectedApp(context, event.appInfo.packageName)

                    if (isSystemApp || isProtectedApp) {
                        showToast(context, "システムアプリは削除できません")
                    } else {
                        event.appInfo.delete(context = context)
                    }
                }

                is HomeUiEvent.OnShowScaleSliderButtonClick -> {
                    UnitySendMessage("SliderManeger", "ShowObject", "")
                    viewModel.setIsShowScaleSliderButtonShown(true)
                }

                is HomeUiEvent.OnCloseScaleSliderButtonClick -> {
                    UnitySendMessage("SliderManeger", "HideObject", "")
                    viewModel.setIsShowScaleSliderButtonShown(false)
                }

                is HomeUiEvent.OnSetDefaultModelButtonClick -> {
                    scope.launch {
                        val defaultModelFilePath = FileUtils.copyVrmFileFromAssets(context)?.absolutePath
                        val isDefaultModelFile =
                            uiState.currentUserSettings.modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) } ?: false

                        if (!isDefaultModelFile) {
                            viewModel.setIsModelLoading(true)
                            viewModel.saveModelFilePath(ModelFilePath(defaultModelFilePath))
                        }
                    }
                }

                is HomeUiEvent.OnOpenDocumentButtonClick -> {
                    if (isModelChangeWarningFirstShown) {
                        openDocumentLauncher.launch(arrayOf("*/*"))
                    } else {
                        viewModel.setIsModelChangeWarningDialogShown(true)
                    }
                }

                is HomeUiEvent.OnNavigateSettingsButtonClick -> {
                    latestNavigateToSettingsScreen()
                }

                is HomeUiEvent.OnModelChangeWarningDialogConfirm -> {
                    viewModel.setIsModelChangeWarningDialogShown(false)
                    viewModel.markModelChangeWarningFirstShown()
                    openDocumentLauncher.launch(arrayOf("*/*"))
                }

                is HomeUiEvent.OnModelChangeWarningDialogDismiss -> {
                    viewModel.setIsModelChangeWarningDialogShown(false)
                }

                is HomeUiEvent.OnAppSearchQueryChange -> {
                    viewModel.setAppSearchQuery(event.query)
                }

                is HomeUiEvent.OnAppListSheetSwipeUp -> {
                    scope.launch {
                        viewModel.changeIsAppListBottomSheetOpened(true)
                        appListSheetState.show()
                    }
                }

                is HomeUiEvent.OnAppListSheetSwipeDown -> {
                    scope.launch {
                        appListSheetState.hide()
                    }.invokeOnCompletion {
                        if (!appListSheetState.isVisible) {
                            viewModel.changeIsAppListBottomSheetOpened(false)
                        }
                    }
                }

                is HomeUiEvent.OnAddWidgetButtonClick -> {
                    scope.launch {
                        viewModel.changeIsWidgetListBottomSheetOpened(true)
                        widgetListSheetState.show()
                    }
                }

                is HomeUiEvent.OnWidgetListSheetSwipeDown -> {
                    scope.launch {
                        widgetListSheetState.hide()
                    }.invokeOnCompletion {
                        if (!widgetListSheetState.isVisible) {
                            viewModel.changeIsWidgetListBottomSheetOpened(false)
                        }
                    }
                }

                is HomeUiEvent.OnDisplayModelContentSwipeLeft -> {
                    UnitySendMessage("IKAnimationController", "TriggerExitScreenAnimation", "")
                    viewModel.setCurrentPage(PageContent.Widget)
                }

                is HomeUiEvent.OnWidgetContentSwipeRight -> {
                    UnitySendMessage("IKAnimationController", "TriggerEnterScreenAnimation", "")
                    viewModel.setCurrentPage(PageContent.DisplayModel)
                }

                is HomeUiEvent.OnDisplayModelContentClick -> {
                    UnitySendMessage("IKAnimationController", "MoveLookat", "${event.x},${event.y}")
                }

                is HomeUiEvent.OnDisplayModelContentLongClick -> {
                    UnitySendMessage("VRMAnimationController", "TriggerTouchAnimation", "")
                }

                is HomeUiEvent.OnAllWidgetListWidgetClick -> {
                    viewModel.selectWidget(
                        widgetInfo = event.widgetInfo,
                        context = context,
                        configureWidgetLauncher = configureWidgetLauncher,
                        bindWidgetLauncher = bindWidgetLauncher,
                    )
                    scope.launch {
                        widgetListSheetState.hide()
                    }.invokeOnCompletion {
                        if (!widgetListSheetState.isVisible) {
                            viewModel.changeIsWidgetListBottomSheetOpened(false)
                        }
                    }
                }

                is HomeUiEvent.OnWidgetContentLongClick -> {
                    viewModel.changeIsEditMode(true)
                }

                is HomeUiEvent.OnCompleteEditButtonClick -> {
                    viewModel.saveWidgetList()
                    viewModel.changeIsEditMode(false)
                }

                is HomeUiEvent.OnDeleteWidgetBadgeClick -> {
                    viewModel.deleteWidget(event.widgetInfo)
                }

                is HomeUiEvent.OnResizeWidgetBadgeClick -> {
                    viewModel.changeIsWidgetResizing(true)
                    viewModel.changeResizingWidget(event.widgetInfo)
                    viewModel.deleteWidget(event.widgetInfo)
                }

                is HomeUiEvent.OnWidgetResizeBottomSheetClose -> {
                    viewModel.changeIsWidgetResizing(false)
                    viewModel.addDisplayedWidgetList(event.widgetInfo)
                    viewModel.changeResizingWidget(null)
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

    if (uiState.isAppListSheetOpened) {
        AppListSheet(
            appList = homeAppList,
            appListSheetState = appListSheetState,
            uiState = uiState,
            onEvent = onEvent,
        )
    }

    if (uiState.isWidgetListSheetOpened) {
        WidgetListSheet(
            widgetListSheetState = widgetListSheetState,
            groupedWidgetInfoMap = groupedWidgetInfoMap,
            onEvent = onEvent,
            modifier = Modifier.padding(
                top = topPaddingValue,
            ),
        )
    }

    if (uiState.isWidgetResizing) {
        uiState.resizeWidget?.let { widgetInfo ->
            WidgetResizeBottomSheet(
                widgetInfo = widgetInfo,
                createWidgetView = createWidgetView,
                close = { onEvent(HomeUiEvent.OnWidgetResizeBottomSheetClose(it)) },
            )
        }
    }

    Box(
        modifier = modifier
            .padding(
                top = topPaddingValue,
                bottom = bottomPaddingValue + Paddings.Medium,
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
