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
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.PlacedApp
import io.github.kei_1111.withmo.core.designsystem.component.PlacedWidget
import io.github.kei_1111.withmo.core.designsystem.component.WithmoIconButton
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.toShape
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeScreenDimensions
import io.github.kei_1111.withmo.feature.home.HomeState

@Composable
internal fun PlaceableItemContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // App Composableの高さ
    val appHeight = CommonDimensions.AppIconSize + Paddings.Large
    // RowAppList Composableの高さ
    val rowAppListHeight = appHeight + Paddings.ExtraSmall * 2

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
                            startPadding = Paddings.Medium,
                            endPadding = Paddings.Medium,
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
                            startPadding = Paddings.Medium,
                            endPadding = Paddings.Medium,
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
            EditPlaceableItemContent(
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
private fun EditPlaceableItemContent(
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
        AddPlaceableItemButton(
            onClick = { onAction(HomeAction.OnAddPlaceableItemButtonClick) },
        )
        CompleteEditButton(
            onClick = { onAction(HomeAction.OnCompleteEditButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
    }
}

@Composable
private fun AddPlaceableItemButton(
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

@Preview
@Composable
private fun PlaceableItemContentightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        PlaceableItemContent(
            state = HomeState(
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
            state = HomeState(
                isEditMode = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun EditPlaceableItemContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        EditPlaceableItemContent(
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun EditPlaceableItemContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        EditPlaceableItemContent(
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
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
