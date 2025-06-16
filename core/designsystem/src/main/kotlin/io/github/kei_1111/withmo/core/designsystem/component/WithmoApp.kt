package io.github.kei_1111.withmo.core.designsystem.component

import android.os.Build
import androidx.annotation.RequiresApi
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
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemDarkPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemLightPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteOrder
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import io.github.kei_1111.withmo.core.util.ktx.toPx

@Composable
fun WithmoApp(
    withmoAppInfo: WithmoAppInfo,
    appIconSize: Float,
    appIconShape: Shape,
    topPadding: Dp,
    bottomPadding: Dp,
    startPadding: Dp,
    endPadding: Dp,
    isEditMode: Boolean,
    deleteApp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    // App ComposableのSize
    val appSize = appIconSize.dp + Paddings.Large

    PlaceableItemContainer(
        placeableItem = withmoAppInfo,
        width = appSize,
        height = appSize,
        topPaddingPx = topPadding.toPx(),
        bottomPaddingPx = bottomPadding.toPx(),
        startPaddingPx = startPadding.toPx(),
        endPaddingPx = endPadding.toPx(),
        isEditMode = isEditMode,
        modifier = modifier,
        deletePlaceableItem = deleteApp,
    ) {
        App(
            appInfo = withmoAppInfo.info,
            appIconSize = appIconSize,
            appIconShape = appIconShape,
            onClick = { withmoAppInfo.info.launch(context) },
            onLongClick = { withmoAppInfo.info.delete(context) },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun WithmoAppLightPreview() {
    DesignSystemLightPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        WithmoApp(
            withmoAppInfo = WithmoAppInfo(
                info = AppInfo(
                    appIcon = appIcon,
                    label = "withmo",
                    packageName = "io.github.kei_1111.withmo.app",
                    notification = false,
                ),
                favoriteOrder = FavoriteOrder.NotFavorite,
                position = Offset.Zero,
            ),
            appIconSize = AppConstants.DefaultAppIconSize,
            appIconShape = CircleShape,
            topPadding = 0.dp,
            bottomPadding = 0.dp,
            startPadding = 0.dp,
            endPadding = 0.dp,
            isEditMode = false,
            deleteApp = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun WithmoAppDarkPreview() {
    DesignSystemDarkPreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        WithmoApp(
            withmoAppInfo = WithmoAppInfo(
                info = AppInfo(
                    appIcon = appIcon,
                    label = "withmo",
                    packageName = "io.github.kei_1111.withmo.app",
                    notification = false,
                ),
                favoriteOrder = FavoriteOrder.NotFavorite,
                position = Offset.Zero,
            ),
            appIconSize = AppConstants.DefaultAppIconSize,
            appIconShape = RoundedCornerShape(3.dp),
            topPadding = 0.dp,
            bottomPadding = 0.dp,
            startPadding = 0.dp,
            endPadding = 0.dp,
            isEditMode = true,
            deleteApp = {},
        )
    }
}
