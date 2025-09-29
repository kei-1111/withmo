package io.github.kei_1111.withmo.feature.home.component.page_content

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
import io.github.kei_1111.withmo.core.designsystem.component.PlacedApp
import io.github.kei_1111.withmo.core.designsystem.component.PlacedWidget
import io.github.kei_1111.withmo.core.designsystem.component.WithmoIconButton
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeScreenDimensions
import io.github.kei_1111.withmo.feature.home.HomeState

@Composable
internal fun PlaceableItemContent(
    state: HomeState.Stable,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // App Composableの高さ
    val appHeight = 76.dp
    // RowAppList Composableの高さ
    val rowAppListHeight = appHeight + 4.dp * 2

    val topPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
    val bottomPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier,
    ) {
        state.placedItemList.forEach { placeableItem ->
            key(placeableItem.id) {
                when (placeableItem) {
                    is PlacedWidgetInfo -> {
                        PlacedWidget(
                            placedWidgetInfo = placeableItem,
                            startPadding = 16.dp,
                            endPadding = 16.dp,
                            topPadding = topPaddingValue,
                            bottomPadding = bottomPaddingValue + rowAppListHeight + HomeScreenDimensions.PageIndicatorSpaceHeight,
                            isEditMode = state.isEditMode,
                            onDeleteBadgeClick = { onAction(HomeAction.OnDeletePlaceableItemBadgeClick(placeableItem)) },
                            onResizeBadgeClick = { onAction(HomeAction.OnResizeWidgetBadgeClick(placeableItem)) },
                        )
                    }

                    is PlacedAppInfo -> {
                        PlacedApp(
                            placedAppInfo = placeableItem,
                            appIconShape = state.currentUserSettings.appIconSettings.appIconShape.toShape(
                                state.currentUserSettings.appIconSettings.roundedCornerPercent,
                            ),
                            isNotificationBadgeShown =
                            state.currentUserSettings.notificationSettings.isNotificationBadgeEnabled,
                            topPadding = topPaddingValue,
                            bottomPadding = bottomPaddingValue + rowAppListHeight + HomeScreenDimensions.PageIndicatorSpaceHeight,
                            startPadding = 16.dp,
                            endPadding = 16.dp,
                            isEditMode = state.isEditMode,
                            onAppClick = { onAction(HomeAction.OnAppClick(placeableItem.info)) },
                            onAppLongClick = { onAction(HomeAction.OnAppLongClick(placeableItem.info)) },
                            onDeleteBadgeClick = { onAction(HomeAction.OnDeletePlaceableItemBadgeClick(placeableItem)) },
                        )
                    }
                }
            }
        }

        if (state.isEditMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bottomPaddingValue + 4.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Spacer(
                    modifier = Modifier.weight(1f),
                )
                AddPlaceableItemButton(
                    onClick = { onAction(HomeAction.OnAddPlaceableItemButtonClick) },
                )
                CompleteEditButton(
                    onClick = { onAction(HomeAction.OnCompleteEditButtonClick) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun AddPlaceableItemButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoIconButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
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
            .height(56.dp)
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

@Preview
@Composable
private fun PlaceableItemContentightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        PlaceableItemContent(
            state = HomeState.Stable(
                isEditMode = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun PlaceableItemContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        PlaceableItemContent(
            state = HomeState.Stable(
                isEditMode = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun AddPlaceableItemButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        AddPlaceableItemButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun AddPlaceableItemButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        AddPlaceableItemButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun CompleteEditButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        CompleteEditButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun CompleteEditButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        CompleteEditButton(
            onClick = {},
        )
    }
}
