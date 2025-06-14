package io.github.kei_1111.withmo.feature.home.component.page_content

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoIconButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoWidget
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeScreenDimensions
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.preview.HomeDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.home.preview.HomeLightPreviewEnvironment

@Composable
internal fun WidgetContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // App Composableの高さ
    val appHeight = state.currentUserSettings.appIconSettings.appIconSize.dp + Paddings.Large
    // RowAppList Composableの高さ
    val rowAppListHeight = appHeight + Paddings.Small * 2 + Paddings.ExtraSmall * 2

    val topPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
    val bottomPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier,
    ) {
        state.widgetList.forEach { withmoWidgetInfo ->
            key(withmoWidgetInfo.widgetInfo.id) {
                WithmoWidget(
                    withmoWidgetInfo = withmoWidgetInfo,
                    startPadding = Paddings.Medium,
                    endPadding = Paddings.Medium,
                    topPadding = topPaddingValue,
                    bottomPadding = bottomPaddingValue + rowAppListHeight + HomeScreenDimensions.PageIndicatorSpaceHeight,
                    isEditMode = state.isEditMode,
                    deleteWidget = { onAction(HomeAction.OnDeleteWidgetBadgeClick(withmoWidgetInfo)) },
                    resizeWidget = { onAction(HomeAction.OnResizeWidgetBadgeClick(withmoWidgetInfo)) },
                )
            }
        }
        if (state.isEditMode) {
            EditWidgetContent(
                onAction = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bottomPaddingValue + Paddings.ExtraSmall)
                    .padding(horizontal = Paddings.Medium),
            )
        }
    }
}

@Composable
private fun EditWidgetContent(
    onAction: (HomeAction) -> Unit,
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
            onClick = { onAction(HomeAction.OnAddWidgetButtonClick) },
        )
        CompleteEditButton(
            onClick = { onAction(HomeAction.OnCompleteEditButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
    }
}

@Composable
private fun AddWidgetButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoIconButton(
        onClick = onClick,
        modifier = modifier.size(CommonDimensions.SettingItemHeight),
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Icon(
            imageVector = Icons.Rounded.Widgets,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun WidgetContentLightPreview() {
    HomeLightPreviewEnvironment {
        WidgetContent(
            state = HomeState(
                isEditMode = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun WidgetContentDarkPreview() {
    HomeDarkPreviewEnvironment {
        WidgetContent(
            state = HomeState(
                isEditMode = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun EditWidgetContentLightPreview() {
    HomeLightPreviewEnvironment {
        EditWidgetContent(
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun EditWidgetContentDarkPreview() {
    HomeDarkPreviewEnvironment {
        EditWidgetContent(
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun AddWidgetButtonLightPreview() {
    HomeLightPreviewEnvironment {
        AddWidgetButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun AddWidgetButtonDarkPreview() {
    HomeDarkPreviewEnvironment {
        AddWidgetButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun CompleteEditButtonLightPreview() {
    HomeLightPreviewEnvironment {
        CompleteEditButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun CompleteEditButtonDarkPreview() {
    HomeDarkPreviewEnvironment {
        CompleteEditButton(
            onClick = {},
        )
    }
}
