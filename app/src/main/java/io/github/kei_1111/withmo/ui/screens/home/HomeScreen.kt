package io.github.kei_1111.withmo.ui.screens.home

import android.app.Activity.RESULT_OK
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
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
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import io.github.kei_1111.withmo.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.common.unity.UnityMethod
import io.github.kei_1111.withmo.common.unity.UnityObject
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.domain.model.user_settings.SortType
import io.github.kei_1111.withmo.ui.composition.LocalAppWidgetHost
import io.github.kei_1111.withmo.ui.composition.LocalAppWidgetManager
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
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Suppress("ModifierMissing", "LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onNavigateSettingsButtonClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val appListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val widgetListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val appList by viewModel.appList.collectAsStateWithLifecycle()

    val appWidgetHost = LocalAppWidgetHost.current
    val appWidgetManager = LocalAppWidgetManager.current

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
        uiState.pendingWidgetInfo?.let { widgetInfo ->
            if (result.resultCode == RESULT_OK) {
                viewModel.addDisplayedWidgetList(WithmoWidgetInfo(widgetInfo))
            } else {
                appWidgetHost.deleteAppWidgetId(widgetInfo.id)
            }
        }
    }

    val bindWidgetLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        uiState.pendingWidgetInfo?.let { widgetInfo ->
            if (result.resultCode == RESULT_OK) {
                if (widgetInfo.info.configure != null) {
                    val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
                    intent.component = widgetInfo.info.configure
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetInfo.id)
                    configureWidgetLauncher.launch(intent)
                } else {
                    viewModel.addDisplayedWidgetList(WithmoWidgetInfo(widgetInfo))
                }
            } else {
                appWidgetHost.deleteAppWidgetId(widgetInfo.id)
            }
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

    val currentOnNavigateSettingsButtonClick by rememberUpdatedState(onNavigateSettingsButtonClick)

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is HomeUiEvent.OnAppClick -> {
                    if (event.appInfo.packageName == context.packageName) {
                        currentOnNavigateSettingsButtonClick()
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
                    AndroidToUnityMessenger.sendMessage(UnityObject.SliderManeger, UnityMethod.ShowObject, "")
                    viewModel.setIsShowScaleSliderButtonShown(true)
                }

                is HomeUiEvent.OnCloseScaleSliderButtonClick -> {
                    AndroidToUnityMessenger.sendMessage(UnityObject.SliderManeger, UnityMethod.HideObject, "")
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
                    currentOnNavigateSettingsButtonClick()
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
                    AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerExitScreenAnimation, "")
                    viewModel.setCurrentPage(PageContent.Widget)
                }

                is HomeUiEvent.OnWidgetContentSwipeRight -> {
                    AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerEnterScreenAnimation, "")
                    viewModel.setCurrentPage(PageContent.DisplayModel)
                }

                is HomeUiEvent.OnDisplayModelContentClick -> {
                    AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.MoveLookat, "${event.x},${event.y}")
                }

                is HomeUiEvent.OnDisplayModelContentLongClick -> {
                    AndroidToUnityMessenger.sendMessage(UnityObject.VRMAnimationController, UnityMethod.TriggerTouchAnimation, "")
                }

                is HomeUiEvent.OnWidgetListSheetItemClick -> {
                    val widgetId = appWidgetHost.allocateAppWidgetId()
                    val provider = event.widgetInfo.provider
                    val options = bundleOf(
                        AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH to event.widgetInfo.minWidth,
                        AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH to event.widgetInfo.minWidth,
                        AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT to event.widgetInfo.minHeight,
                        AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT to event.widgetInfo.minHeight,
                    )
                    val success = appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, provider, options)

                    val widgetInfo = WidgetInfo(widgetId, event.widgetInfo)
                    viewModel.setPendingWidget(widgetInfo)

                    if (success) {
                        if (event.widgetInfo.configure != null) {
                            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE).apply {
                                component = event.widgetInfo.configure
                                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                            }

                            val activityInfo = try {
                                context.packageManager.getActivityInfo(event.widgetInfo.configure, 0)
                            } catch (e: PackageManager.NameNotFoundException) {
                                Log.e("HomeScreen", "Failed to retrieve activity info for widget configuration: ${event.widgetInfo.configure}", e)
                                null
                            }

                            if (activityInfo != null && activityInfo.exported) {
                                configureWidgetLauncher.launch(intent)
                            } else {
                                viewModel.addDisplayedWidgetList(WithmoWidgetInfo(widgetInfo))
                            }
                        } else {
                            viewModel.addDisplayedWidgetList(WithmoWidgetInfo(widgetInfo))
                        }
                    } else {
                        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, provider)
                        }
                        bindWidgetLauncher.launch(intent)
                    }
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
                    viewModel.deleteWidget(event.withmoWidgetInfo)
                }

                is HomeUiEvent.OnResizeWidgetBadgeClick -> {
                    viewModel.changeIsWidgetResizing(true)
                    viewModel.changeResizingWidget(event.withmoWidgetInfo)
                    viewModel.deleteWidget(event.withmoWidgetInfo)
                }

                is HomeUiEvent.OnWidgetResizeBottomSheetClose -> {
                    viewModel.changeIsWidgetResizing(false)
                    viewModel.addDisplayedWidgetList(event.withmoWidgetInfo)
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
            onEvent = onEvent,
            modifier = Modifier.padding(
                top = topPaddingValue,
            ),
        )
    }

    if (uiState.isWidgetResizing) {
        uiState.resizeWidget?.let { widgetInfo ->
            WidgetResizeBottomSheet(
                withmoWidgetInfo = widgetInfo,
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
