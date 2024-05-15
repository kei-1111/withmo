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
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withmo.R
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.SortMode
import com.example.withmo.ui.component.homescreen.Clock
import com.example.withmo.ui.component.homescreen.RowAppList
import com.example.withmo.ui.screens.applist.AppListScreen
import com.example.withmo.until.BOTTOM_PADDING
import com.example.withmo.until.ICON_SIZE
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayer.UnitySendMessage
import kotlin.math.roundToInt

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    unityPlayer: UnityPlayer?,
    homeViewModel: HomeViewModel = hiltViewModel(),
    showSetting: () -> Unit,
    appList: List<AppInfo>,
) {
    val context = LocalContext.current

    val uiState = homeViewModel.uiState

    // swipe
    val height = LocalConfiguration.current.screenHeightDp + 100
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { height.dp.toPx() }
    val anchors = mapOf(sizePx to 0, 0f to 1)

    var homeAppList by remember { mutableStateOf(appList) }

    val paddingValues = WindowInsets.safeGestures.asPaddingValues()

    LaunchedEffect(appList) {
        homeAppList = when (homeViewModel.getUserSetting().sortMode) {
            SortMode.USE_COUNT -> appList.sortedByDescending { it.useCount }

            SortMode.ALPHABETICAL -> appList.sortedBy { it.label }
        }
    }

    val boxModifier =
        if (!uiState.showScaleSlider && uiState.finishSprashScreen) {
            Modifier
                .fillMaxSize()
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.4f) },
                    orientation = Orientation.Vertical
                )
        } else {
            Modifier.fillMaxSize()
        }

    Box(
        modifier = boxModifier
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                FrameLayout(context).apply {
                    unityPlayer?.let { addView(it.rootView) }
                }
            },
        )

        if (uiState.finishSprashScreen) {
            if (homeViewModel.getUserSetting().showClock) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            start = paddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Rtl)
                        )
                ) {
                    Clock(
                        clockMode = homeViewModel.getUserSetting().clockMode,
                        year = homeViewModel.getTime()[0],
                        month = homeViewModel.getTime()[1],
                        day = homeViewModel.getTime()[2],
                        dayOfWeek = homeViewModel.getTime()[5],
                        hour = homeViewModel.getTime()[3],
                        minute = homeViewModel.getTime()[4],
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = BOTTOM_PADDING),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.large_padding)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.large_padding))
                ) {
                    if(homeViewModel.getUserSetting().showScaleSliderButton) {
                        IconButton(
                            onClick = {
                                if (uiState.showScaleSlider) {
                                    UnitySendMessage("Slidermaneger", "HideSlider", "")
                                    homeViewModel.setShowScaleSlider(false)
                                } else {
                                    UnitySendMessage("Slidermaneger", "ShowSlider", "")
                                    homeViewModel.setShowScaleSlider(true)
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(dimensionResource(id = R.dimen.shadow), CircleShape)
                                .background(MaterialTheme.colorScheme.surface, CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_man),
                                contentDescription = "model scale",
                                modifier = Modifier
                                    .size(ICON_SIZE)
                                    .padding(dimensionResource(id = R.dimen.small_padding))
                            )
                        }
                    }
                    if(homeViewModel.getUserSetting().showSortButton) {
                        IconButton(
                            onClick = {
                                if (uiState.popupExpanded) {
                                    homeViewModel.setPopupExpanded(false)
                                } else {
                                    homeViewModel.setPopupExpanded(true)
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(dimensionResource(id = R.dimen.shadow), CircleShape)
                                .background(MaterialTheme.colorScheme.surface, CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_sort),
                                contentDescription = "model scale",
                                modifier = Modifier
                                    .size(ICON_SIZE)
                                    .padding(dimensionResource(id = R.dimen.small_padding))
                            )
                        }
                    }
                }
                RowAppList(
                    context = context,
                    appList = homeAppList,
                    appIconSize = homeViewModel.getUserSetting().appIconSize,
                    appIconPadding = homeViewModel.getUserSetting().appIconPadding,
                    showAppName = homeViewModel.getUserSetting().showAppName,
                    showSetting = showSetting,
                )
            }
        }
        if (uiState.popupExpanded) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { homeViewModel.setPopupExpanded(false) },
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.5F),
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = dimensionResource(id = R.dimen.shadow)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.medium_padding)),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_padding))
                        
                    ){
                        Text(
                            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.medium_padding)),
                            text = "アプリの並び順",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    homeViewModel.setPopupExpanded(false)
                                    homeAppList = homeAppList
                                        .sortedByDescending { it.useCount }
                                    homeViewModel.setUserSetting(
                                        homeViewModel
                                            .getUserSetting()
                                            .copy(sortMode = SortMode.USE_COUNT)
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "使用順",
                                modifier = Modifier.weight(1F),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            RadioButton(
                                modifier = Modifier.size(10.dp),
                                selected = homeViewModel.getUserSetting().sortMode == SortMode.USE_COUNT,
                                onClick = {
                                    homeViewModel.setPopupExpanded(false)
                                    homeAppList = homeAppList
                                        .sortedByDescending { it.useCount }
                                    homeViewModel.setUserSetting(
                                        homeViewModel.getUserSetting()
                                            .copy(sortMode = SortMode.USE_COUNT)
                                    )
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                )
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    homeViewModel.setPopupExpanded(false)
                                    homeAppList = homeAppList.sortedBy { it.label }
                                    homeViewModel.setUserSetting(
                                        homeViewModel
                                            .getUserSetting()
                                            .copy(sortMode = SortMode.ALPHABETICAL)
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ABC順",
                                modifier = Modifier.weight(1F),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            RadioButton(
                                modifier = Modifier.size(10.dp),
                                selected = homeViewModel.getUserSetting().sortMode == SortMode.ALPHABETICAL,
                                onClick = {
                                    homeViewModel.setPopupExpanded(false)
                                    homeAppList = homeAppList.sortedBy { it.label }
                                    homeViewModel.setUserSetting(
                                        homeViewModel.getUserSetting()
                                            .copy(sortMode = SortMode.ALPHABETICAL)
                                    )
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                )
                            )
                        }
                    }
                }
            }
        }

        if (!uiState.showScaleSlider && uiState.finishSprashScreen) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .clip(shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
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

