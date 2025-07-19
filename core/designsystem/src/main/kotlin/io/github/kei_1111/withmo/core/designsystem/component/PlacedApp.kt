package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.util.ktx.toPx

@Composable
fun PlacedApp(
    placedAppInfo: PlacedAppInfo,
    appIconShape: Shape,
    isNotificationBadgeShown: Boolean,
    topPadding: Dp,
    bottomPadding: Dp,
    startPadding: Dp,
    endPadding: Dp,
    isEditMode: Boolean,
    onAppClick: () -> Unit,
    onAppLongClick: () -> Unit,
    onDeleteBadgeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    // App ComposableのSize
    val appSize = CommonDimensions.AppIconSize + Paddings.Large

    PlaceableItemContainer(
        placeableItem = placedAppInfo,
        width = appSize,
        height = appSize,
        topPaddingPx = topPadding.toPx(),
        bottomPaddingPx = bottomPadding.toPx(),
        startPaddingPx = startPadding.toPx(),
        endPaddingPx = endPadding.toPx(),
        isEditMode = isEditMode,
        modifier = modifier,
        onDeleteBadgeClick = onDeleteBadgeClick,
    ) {
        App(
            appInfo = placedAppInfo.info,
            appIconShape = appIconShape,
            isNotificationBadgeShown = isNotificationBadgeShown,
            isAppNameShown = false,
            onClick = onAppClick,
            onLongClick = onAppLongClick,
        )
    }
}

@Composable
@Preview
private fun WithmoAppLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        PlacedApp(
            placedAppInfo = PlacedAppInfo(
                id = "withmo-app",
                info = AppInfo(
                    appIcon = appIcon,
                    label = "withmo",
                    packageName = "io.github.kei_1111.withmo.app",
                    notification = false,
                ),
                position = Offset.Zero,
            ),
            appIconShape = CircleShape,
            isNotificationBadgeShown = false,
            topPadding = 0.dp,
            bottomPadding = 0.dp,
            startPadding = 0.dp,
            endPadding = 0.dp,
            isEditMode = false,
            onAppClick = {},
            onAppLongClick = {},
            onDeleteBadgeClick = {},
        )
    }
}

@Composable
@Preview
private fun WithmoAppDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        PlacedApp(
            placedAppInfo = PlacedAppInfo(
                id = "withmo-app-dark",
                info = AppInfo(
                    appIcon = appIcon,
                    label = "withmo",
                    packageName = "io.github.kei_1111.withmo.app",
                    notification = true,
                ),
                position = Offset.Zero,
            ),
            appIconShape = RoundedCornerShape(3.dp),
            isNotificationBadgeShown = true,
            topPadding = 0.dp,
            bottomPadding = 0.dp,
            startPadding = 0.dp,
            endPadding = 0.dp,
            isEditMode = true,
            onAppClick = {},
            onAppLongClick = {},
            onDeleteBadgeClick = {},
        )
    }
}
