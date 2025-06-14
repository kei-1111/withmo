package io.github.kei_1111.withmo.feature.home.component.page_content

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeScreenDimensions
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.PageContent
import io.github.kei_1111.withmo.feature.home.preview.HomeDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.home.preview.HomeLightPreviewEnvironment

@Suppress("LongMethod")
@Composable
internal fun PagerContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val pagerState = rememberPagerState(
        initialPage = state.currentPage.ordinal,
        pageCount = { PageContent.entries.size },
    )

    LaunchedEffect(pagerState, onAction) {
        var isFirstCollect = true
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (isFirstCollect) {
                isFirstCollect = false
            } else {
                when (page) {
                    PageContent.DisplayModel.ordinal -> {
                        onAction(HomeAction.OnWidgetContentSwipeRight)
                    }

                    PageContent.Widget.ordinal -> {
                        onAction(HomeAction.OnDisplayModelContentSwipeLeft)
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
            userScrollEnabled = !state.isEditMode,
        ) { page ->
            when (page) {
                PageContent.DisplayModel.ordinal -> {
                    DisplayModelContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        val normalizedX = it.x / screenWidthPx
                                        val normalizedY = it.y / screenHeightPx

                                        onAction(HomeAction.OnDisplayModelContentClick(normalizedX, normalizedY))
                                    },
                                    onLongPress = { onAction(HomeAction.OnDisplayModelContentLongClick) },
                                )
                            },
                    )
                }

                PageContent.Widget.ordinal -> {
                    WidgetContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { onAction(HomeAction.OnWidgetContentLongClick) },
                                )
                            },
                    )
                }
            }
        }
        if (!state.isEditMode) {
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
            val color = if (currentPage == iteration) {
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PagerContentLightPreview() {
    HomeLightPreviewEnvironment {
        PagerContent(
            state = HomeState(),
            onAction = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PagerContentDarkPreview() {
    HomeDarkPreviewEnvironment {
        PagerContent(
            state = HomeState(
                isEditMode = true,
                currentPage = PageContent.Widget,
            ),
            onAction = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PageIndicatorLightPreview() {
    HomeLightPreviewEnvironment {
        PageIndicator(
            pageCount = 3,
            currentPage = 1,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PageIndicatorDarkPreview() {
    HomeDarkPreviewEnvironment {
        PageIndicator(
            pageCount = 3,
            currentPage = 1,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
