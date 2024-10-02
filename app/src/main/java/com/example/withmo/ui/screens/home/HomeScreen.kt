package com.example.withmo.ui.screens.home

import android.os.Build
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.SortMode
import com.example.withmo.ui.component.homescreen.Clock
import com.example.withmo.ui.component.homescreen.RowAppList
import com.example.withmo.ui.screens.applist.AppListScreen
import com.example.withmo.ui.theme.UiConfig
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayer.UnitySendMessage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt

@Suppress("ModifierMissing", "LongMethod", "CyclomaticComplexity", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    unityPlayer: UnityPlayer?,
    showSetting: () -> Unit,
    appList: ImmutableList<AppInfo>,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val uiState = viewModel.uiState
    val userSetting by viewModel.userSetting.collectAsState()

    // swipe
    val height = context.resources.displayMetrics.heightPixels
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { height.dp.toPx() }
    val anchors = mapOf(sizePx to 0, 0f to 1)

    var homeAppList by remember { mutableStateOf(appList) }

    val paddingValues = WindowInsets.safeGestures.asPaddingValues()

    LaunchedEffect(appList) {
        homeAppList = when (userSetting.sortMode) {
            SortMode.USE_COUNT -> appList.sortedByDescending { it.useCount }.toPersistentList()

            SortMode.ALPHABETICAL -> appList.sortedBy { it.label }.toPersistentList()
        }
    }

    val boxModifier =
        if (!uiState.showScaleSlider && uiState.finishSplashScreen) {
            Modifier
                .fillMaxSize()
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    orientation = Orientation.Vertical,
                )
        } else {
            Modifier.fillMaxSize()
        }

    Box(
        modifier = boxModifier,
    ) {
        unityPlayer?.let {
            UnityScreen(
                unityPlayer = unityPlayer,
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (uiState.finishSplashScreen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                    ),
            ) {
                if (userSetting.showClock) {
                    Clock(
                        clockMode = userSetting.clockMode,
                        dateTimeInfo = viewModel.getTime(),
                        modifier = Modifier.padding(start = UiConfig.MediumPadding),
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End,
                ) {
                    Column(
                        modifier = Modifier.padding(UiConfig.MediumPadding),
                        verticalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
                    ) {
                        if (userSetting.showScaleSliderButton) {
                            HomeScreenIconButton(
                                onClick = {
                                    if (uiState.showScaleSlider) {
                                        UnitySendMessage("Slidermaneger", "HideSlider", "")
                                        viewModel.setShowScaleSlider(false)
                                    } else {
                                        UnitySendMessage("Slidermaneger", "ShowSlider", "")
                                        viewModel.setShowScaleSlider(true)
                                    }
                                },
                                icon = Icons.Default.Man,
                            )
                        }
                        if (userSetting.showSortButton) {
                            HomeScreenIconButton(
                                onClick = {
                                    if (uiState.popupExpanded) {
                                        viewModel.setPopupExpanded(false)
                                    } else {
                                        viewModel.setPopupExpanded(true)
                                    }
                                },
                                icon = Icons.Default.Tune,
                            )
                        }
                    }
                    RowAppList(
                        context = context,
                        appList = homeAppList,
                        appIconSize = userSetting.appIconSize,
                        appIconPadding = userSetting.appIconPadding,
                        showAppName = userSetting.showAppName,
                        showSetting = showSetting,
                    )
                }
            }
        }

        if (uiState.popupExpanded) {
            PopupContent(
                onDismissRequest = { viewModel.setPopupExpanded(false) },
                sortByUsageOrder = {
                    viewModel.setPopupExpanded(false)
                    homeAppList = homeAppList.sortedByDescending { it.useCount }.toPersistentList()
                    viewModel.saveUserSetting(
                        userSetting.copy(sortMode = SortMode.USE_COUNT),
                    )
                },
                sortByAlphabeticalOrder = {
                    viewModel.setPopupExpanded(false)
                    homeAppList = homeAppList.sortedBy { it.label }.toPersistentList()
                    viewModel.saveUserSetting(
                        userSetting.copy(sortMode = SortMode.ALPHABETICAL),
                    )
                },
                sortMode = userSetting.sortMode,
            )
        }

        if (!uiState.showScaleSlider && uiState.finishSplashScreen) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    )
                    .clip(shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
            ) {
                AppListScreen(
                    context = context,
                    appList = homeAppList,
                    showSetting = showSetting,
                )
            }
        }
    }
}

@Composable
private fun UnityScreen(
    unityPlayer: UnityPlayer,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            FrameLayout(context).apply {
                unityPlayer?.let { addView(it.rootView) }
            }
        },
    )
}

@Composable
private fun HomeScreenIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .shadow(UiConfig.ShadowElevation, CircleShape)
            .background(MaterialTheme.colorScheme.surface, CircleShape),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}

@Composable
private fun PopupContent(
    onDismissRequest: () -> Unit,
    sortByUsageOrder: () -> Unit,
    sortByAlphabeticalOrder: () -> Unit,
    sortMode: SortMode,
    modifier: Modifier = Modifier,
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = modifier.width(UiConfig.PopupWidth),
            shape = MaterialTheme.shapes.large,
            shadowElevation = UiConfig.ShadowElevation,
        ) {
            Column(
                modifier = Modifier.padding(UiConfig.MediumPadding),
                verticalArrangement = Arrangement.spacedBy(UiConfig.SmallPadding),
            ) {
                Text(
                    modifier = Modifier.padding(vertical = UiConfig.SmallPadding),
                    text = "アプリの並び順",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = sortByUsageOrder,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "使用順",
                        modifier = Modifier.weight(UiConfig.DefaultWeight),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    RadioButton(
                        selected = sortMode == SortMode.USE_COUNT,
                        onClick = sortByUsageOrder,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = sortByAlphabeticalOrder,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "ABC順",
                        modifier = Modifier.weight(UiConfig.DefaultWeight),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    RadioButton(
                        selected = sortMode == SortMode.ALPHABETICAL,
                        onClick = sortByAlphabeticalOrder,
                    )
                }
            }
        }
    }
}
