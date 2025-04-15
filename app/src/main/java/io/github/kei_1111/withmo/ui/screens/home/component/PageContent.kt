package io.github.kei_1111.withmo.ui.screens.home.component

import android.content.Context
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChangeCircle
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.R
import io.github.kei_1111.withmo.common.Constants
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.ui.component.LabelMediumText
import io.github.kei_1111.withmo.ui.component.WithmoIconButton
import io.github.kei_1111.withmo.ui.component.WithmoWidget
import io.github.kei_1111.withmo.ui.screens.home.HomeScreenDimensions
import io.github.kei_1111.withmo.ui.screens.home.HomeUiEvent
import io.github.kei_1111.withmo.ui.screens.home.HomeUiState
import io.github.kei_1111.withmo.ui.theme.dimensions.Alphas
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.ShadowElevations
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

@Suppress("LongMethod")
@Composable
internal fun PagerContent(
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val pagerState = rememberPagerState(
        initialPage = uiState.currentPage.ordinal,
        pageCount = { PageContent.entries.size },
    )

    LaunchedEffect(pagerState, onEvent) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            when (page) {
                PageContent.DisplayModel.ordinal -> {
                    onEvent(HomeUiEvent.OnWidgetContentSwipeRight)
                }

                PageContent.Widget.ordinal -> {
                    onEvent(HomeUiEvent.OnDisplayModelContentSwipeLeft)
                }
            }
        }
    }

    Column(
        modifier = modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(Weights.Medium),
        ) { page ->
            when (page) {
                PageContent.DisplayModel.ordinal -> {
                    DisplayModelContent(
                        uiState = uiState,
                        onEvent = onEvent,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Paddings.Medium)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        val normalizedX = it.x / screenWidthPx
                                        val normalizedY = it.y / screenHeightPx

                                        onEvent(
                                            HomeUiEvent.OnDisplayModelContentClick(
                                                x = normalizedX,
                                                y = normalizedY,
                                            ),
                                        )
                                    },
                                    onLongPress = { onEvent(HomeUiEvent.OnDisplayModelContentLongClick) },
                                )
                            },
                    )
                }

                PageContent.Widget.ordinal -> {
                    WidgetContent(
                        createWidgetView = createWidgetView,
                        uiState = uiState,
                        onEvent = onEvent,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { onEvent(HomeUiEvent.OnWidgetContentLongClick) },
                                )
                            },
                    )
                }
            }
        }
        PageIndicator(
            pageCount = pagerState.pageCount,
            currentPage = pagerState.currentPage,
            isEditMode = uiState.isEditMode,
            exitEditMode = { onEvent(HomeUiEvent.OnCompleteEditButtonClick) },
            openWidgetList = { onEvent(HomeUiEvent.OnAddWidgetButtonClick) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    isEditMode: Boolean,
    exitEditMode: () -> Unit,
    openWidgetList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(HomeScreenDimensions.PageIndicatorSpaceHeight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isEditMode) {
            LabelMediumText(
                text = "＋",
                modifier = Modifier
                    .padding(horizontal = Paddings.Medium)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape,
                    )
                    .clickable { openWidgetList() }
                    .padding(horizontal = Paddings.Medium),
            )
            LabelMediumText(
                text = "編集完了",
                modifier = Modifier
                    .padding(horizontal = Paddings.Medium)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape,
                    )
                    .clickable { exitEditMode() }
                    .padding(horizontal = Paddings.Medium),
            )
        } else {
            repeat(pageCount) { iteration ->
                val color =
                    if (currentPage == iteration) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled)
                    }
                Box(
                    modifier = Modifier
                        .padding(horizontal = Paddings.Medium)
                        .clip(CircleShape)
                        .background(color)
                        .size(HomeScreenDimensions.PageIndicatorSize),
                )
            }
        }
    }
}

@Composable
private fun DisplayModelContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Paddings.Large, Alignment.Bottom),
        horizontalAlignment = Alignment.End,
    ) {
        if (
            uiState.currentUserSettings.sideButtonSettings.isSetDefaultModelButtonShown &&
            uiState.currentUserSettings.modelFilePath.path != null
        ) {
            Surface(
                modifier = Modifier
                    .size(Constants.DefaultAppIconSize.dp)
                    .clickable { onEvent(HomeUiEvent.OnSetDefaultModelButtonClick) },
                shape = CircleShape,
                shadowElevation = ShadowElevations.Medium,
            ) {
                Image(
                    painter = painterResource(R.drawable.alicia_icon),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
        if (uiState.currentUserSettings.sideButtonSettings.isOpenDocumentButtonShown) {
            WithmoIconButton(
                onClick = { onEvent(HomeUiEvent.OnOpenDocumentButtonClick) },
                icon = Icons.Rounded.ChangeCircle,
                modifier = Modifier.size(Constants.DefaultAppIconSize.dp),
            )
        }
        if (uiState.currentUserSettings.sideButtonSettings.isShowScaleSliderButtonShown) {
            WithmoIconButton(
                onClick = { onEvent(HomeUiEvent.OnShowScaleSliderButtonClick) },
                icon = Icons.Rounded.Man,
                modifier = Modifier.size(Constants.DefaultAppIconSize.dp),
            )
        }
    }
}

@Composable
private fun WidgetContent(
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appIconSpaceHeight =
        (uiState.currentUserSettings.appIconSettings.appIconSize + Paddings.AppIconPadding).dp
    val bottomPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier,
    ) {
        uiState.widgetList.forEach { widgetInfo ->
            key(widgetInfo.id) {
                WithmoWidget(
                    widgetInfo = widgetInfo,
                    createWidgetView = createWidgetView,
                    startPadding = Paddings.Medium,
                    endPadding = Paddings.Medium,
                    bottomPadding =
                    bottomPaddingValue + appIconSpaceHeight + HomeScreenDimensions.PageIndicatorSpaceHeight,
                    isEditMode = uiState.isEditMode,
                    deleteWidget = { onEvent(HomeUiEvent.OnDeleteWidgetBadgeClick(widgetInfo)) },
                    resizeWidget = { onEvent(HomeUiEvent.OnResizeWidgetBadgeClick(widgetInfo)) },
                )
            }
        }
    }
}

enum class PageContent {
    DisplayModel,
    Widget,
}
