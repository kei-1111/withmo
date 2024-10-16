package com.example.withmo.ui.screens.home

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.user_settings.SortType
import com.example.withmo.domain.model.user_settings.toShape
import com.example.withmo.ui.theme.BottomSheetShape
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Suppress("ModifierMissing", "LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navigateToSettingsScreen: () -> Unit,
    appList: ImmutableList<AppInfo>,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val homeAppList by remember(appList, uiState.currentUserSettings.sortType) {
        derivedStateOf {
            when (uiState.currentUserSettings.sortType) {
                SortType.USE_COUNT -> appList.sortedByDescending { it.useCount }.toPersistentList()
                SortType.ALPHABETICAL -> appList.sortedBy { it.label }.toPersistentList()
            }
        }
    }

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is HomeUiEvent.SetPopupExpanded -> {
                    viewModel.setPopupExpanded(event.isExpand)
                }

                is HomeUiEvent.SetShowScaleSlider -> {
                    viewModel.setShowScaleSlider(event.isShow)
                }

                is HomeUiEvent.OnValueChangeAppSearchQuery -> {
                    viewModel.setAppSearchQuery(event.query)
                }

                is HomeUiEvent.OnSelectSortByUsageOrder -> {
                    viewModel.setPopupExpanded(false)
                    viewModel.saveSortType(SortType.USE_COUNT)
                }

                is HomeUiEvent.OnSelectSortByAlphabeticalOrder -> {
                    viewModel.setPopupExpanded(false)
                    viewModel.saveSortType(SortType.ALPHABETICAL)
                }

                is HomeUiEvent.OpenBottomSheet -> {
                    scope.launch {
                        viewModel.changeIsBottomSheetOpened(true)
                        sheetState.show()
                    }
                }

                is HomeUiEvent.HideBottomSheet -> {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            viewModel.changeIsBottomSheetOpened(false)
                        }
                    }
                }

                is HomeUiEvent.NavigateToSettingsScreen -> {
                    latestNavigateToSettingsScreen()
                }
            }
        }.launchIn(this)
    }

    AnimatedVisibility(uiState.isFinishSplashScreen) {
        HomeScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            homeAppList = homeAppList,
            sheetState = sheetState,
            context = context,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    homeAppList: ImmutableList<AppInfo>,
    sheetState: SheetState,
    context: Context,
    modifier: Modifier = Modifier,
) {
    val topPaddingValue = WindowInsets.safeGestures.asPaddingValues().calculateTopPadding()
    val bottomPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

    if (uiState.isBottomSheetOpened) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(HomeUiEvent.HideBottomSheet) },
            shape = BottomSheetShape,
            sheetState = sheetState,
            dragHandle = {},
            content = {
                AppList(
                    context = context,
                    appList = homeAppList,
                    appIconShape = uiState.currentUserSettings.appIconSettings.appIconShape.toShape(
                        uiState.currentUserSettings.appIconSettings.roundedCornerPercent,
                    ),
                    appSearchQuery = uiState.appSearchQuery,
                    onEvent = onEvent,
                    navigateToSettingsScreen = { onEvent(HomeUiEvent.NavigateToSettingsScreen) },
                    modifier = modifier,
                )
            },
        )
    }

    Box(
        modifier = modifier
            .padding(
                top = topPaddingValue,
                bottom = bottomPaddingValue + UiConfig.MediumPadding,
            ),
    ) {
        if (!uiState.isShowScaleSlider) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Spacer(
                    modifier = Modifier.weight(UiConfig.DefaultWeight),
                )
                Box(
                    modifier = Modifier
                        .weight(UiConfig.DefaultWeight)
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { change, dragAmount ->
                                    if (dragAmount < UiConfig.BottomSheetShowDragHeight) {
                                        onEvent(HomeUiEvent.OpenBottomSheet)
                                    }
                                },
                            )
                        },
                ) {}
            }
        }
        HomeScreenContent(
            uiState = uiState,
            onEvent = onEvent,
            context = context,
            appList = homeAppList,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
