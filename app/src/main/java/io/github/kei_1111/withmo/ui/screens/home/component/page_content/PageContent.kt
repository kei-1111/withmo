package io.github.kei_1111.withmo.ui.screens.home.component.page_content

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.ui.screens.home.HomeScreenDimensions
import io.github.kei_1111.withmo.ui.screens.home.HomeAction
import io.github.kei_1111.withmo.ui.screens.home.HomeUiState
import io.github.kei_1111.withmo.ui.screens.home.PageContent
import io.github.kei_1111.withmo.ui.theme.dimensions.Alphas
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

@Suppress("LongMethod")
@Composable
internal fun PagerContent(
    uiState: HomeUiState,
    onEvent: (HomeAction) -> Unit,
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
        var isFirstCollect = true
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (isFirstCollect) {
                isFirstCollect = false
            } else {
                when (page) {
                    PageContent.DisplayModel.ordinal -> {
                        onEvent(HomeAction.OnWidgetContentSwipeRight)
                    }

                    PageContent.Widget.ordinal -> {
                        onEvent(HomeAction.OnDisplayModelContentSwipeLeft)
                    }
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
            userScrollEnabled = !uiState.isEditMode,
        ) { page ->
            when (page) {
                PageContent.DisplayModel.ordinal -> {
                    DisplayModelContent(
                        uiState = uiState,
                        onEvent = onEvent,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        val normalizedX = it.x / screenWidthPx
                                        val normalizedY = it.y / screenHeightPx

                                        onEvent(
                                            HomeAction.OnDisplayModelContentClick(
                                                x = normalizedX,
                                                y = normalizedY,
                                            ),
                                        )
                                    },
                                    onLongPress = { onEvent(HomeAction.OnDisplayModelContentLongClick) },
                                )
                            },
                    )
                }

                PageContent.Widget.ordinal -> {
                    WidgetContent(
                        uiState = uiState,
                        onEvent = onEvent,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { onEvent(HomeAction.OnWidgetContentLongClick) },
                                )
                            },
                    )
                }
            }
        }
        if (!uiState.isEditMode) {
            PageIndicator(
                pageCount = pagerState.pageCount,
                currentPage = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(HomeScreenDimensions.PageIndicatorSpaceHeight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
