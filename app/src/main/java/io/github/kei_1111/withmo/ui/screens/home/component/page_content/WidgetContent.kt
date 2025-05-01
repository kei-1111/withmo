package io.github.kei_1111.withmo.ui.screens.home.component.page_content

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.WithmoWidget
import io.github.kei_1111.withmo.ui.component.utils.withmoShadow
import io.github.kei_1111.withmo.ui.screens.home.HomeScreenDimensions
import io.github.kei_1111.withmo.ui.screens.home.HomeUiEvent
import io.github.kei_1111.withmo.ui.screens.home.HomeUiState
import io.github.kei_1111.withmo.ui.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

@Composable
internal fun WidgetContent(
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
        if (uiState.isEditMode) {
            EditWidgetContent(
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = Paddings.ExtraSmall,
                    )
                    .padding(
                        horizontal = Paddings.Medium,
                    ),
            )
        }
    }
}

@Composable
private fun EditWidgetContent(
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        Spacer(
            modifier = Modifier.weight(Weights.Medium),
        )
        AddWidgetButton(
            onClick = { onEvent(HomeUiEvent.OnAddWidgetButtonClick) },
        )
        CompleteEditButton(
            onClick = { onEvent(HomeUiEvent.OnCompleteEditButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
    }
}

@Composable
private fun AddWidgetButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifier
            .size(CommonDimensions.SettingItemHeight)
            .withmoShadow(
                shape = CircleShape,
            ),
    ) {
        Icon(
            imageVector = Icons.Rounded.Widgets,
            contentDescription = null,
        )
    }
}

@Composable
private fun CompleteEditButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .height(CommonDimensions.SettingItemHeight)
            .withmoShadow(
                shape = CircleShape,
            ),
    ) {
        BodyMediumText(
            text = "編集完了",
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
