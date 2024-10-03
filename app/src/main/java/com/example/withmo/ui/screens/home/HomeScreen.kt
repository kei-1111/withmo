package com.example.withmo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.SortMode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToInt

@Suppress("ModifierMissing", "LongMethod")
@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navigateToSettingScreen: () -> Unit,
    appList: ImmutableList<AppInfo>,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // swipe
    val height = context.resources.displayMetrics.heightPixels
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { height.dp.toPx() }
    val anchors = mapOf(sizePx to 0, 0f to 1)

    var homeAppList by remember { mutableStateOf(appList) }

    val paddingValues = WindowInsets.safeGestures.asPaddingValues()
    val topPaddingValue = paddingValues.calculateTopPadding()
    val bottomPaddingValue = paddingValues.calculateBottomPadding()

    LaunchedEffect(appList) {
        homeAppList = when (uiState.currentUserSettings.sortMode) {
            SortMode.USE_COUNT -> appList.sortedByDescending { it.useCount }.toPersistentList()

            SortMode.ALPHABETICAL -> appList.sortedBy { it.label }.toPersistentList()
        }
    }

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
                    homeAppList = homeAppList.sortedByDescending { it.useCount }.toPersistentList()
                    viewModel.saveSortMode(SortMode.USE_COUNT)
                }

                is HomeUiEvent.OnSelectSortByAlphabeticalOrder -> {
                    viewModel.setPopupExpanded(false)
                    homeAppList = homeAppList.sortedBy { it.label }.toPersistentList()
                    viewModel.saveSortMode(SortMode.ALPHABETICAL)
                }
            }
        }.launchIn(this)
    }

    val boxModifier = if (!uiState.isShowScaleSlider) {
        Modifier
            .fillMaxSize()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Vertical,
            )
            .padding(
                top = topPaddingValue,
                bottom = bottomPaddingValue,
            )
    } else {
        Modifier
            .fillMaxSize()
            .padding(
                top = topPaddingValue,
                bottom = bottomPaddingValue,
            )
    }

    AnimatedVisibility(uiState.isFinishSplashScreen) {
        Box(
            modifier = boxModifier,
        ) {
            HomeScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                context = context,
                appList = homeAppList,
                navigateToSettingScreen = navigateToSettingScreen,
                getCurrentTime = viewModel::getTime,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        0,
                        swipeableState.offset.value.roundToInt(),
                    )
                }
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                )
                .clip(shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
        ) {
            AppList(
                context = context,
                appList = homeAppList,
                appSearchQuery = uiState.appSearchQuery,
                onEvent = viewModel::onEvent,
                navigateToSettingScreen = navigateToSettingScreen,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
