package com.example.withmo.ui.screens.home

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.WidgetInfo
import com.example.withmo.domain.model.user_settings.SortType
import com.example.withmo.domain.model.user_settings.toShape
import com.example.withmo.ui.theme.BottomSheetShape
import com.example.withmo.ui.theme.UiConfig
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

@Suppress("LongMethod")
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
    val bottomPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

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
}
