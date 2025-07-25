package io.github.kei_1111.withmo.core.designsystem.component

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.BadgeSizes
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

private const val AppItemLabelMaxLines = 1

@Composable
fun App(
    appInfo: AppInfo,
    isNotificationBadgeShown: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    appIconShape: Shape = CircleShape,
    isAppNameShown: Boolean = true,
) {
    val appIconSize = CommonDimensions.AppIconSize

    Column(
        modifier = modifier
            .size(appIconSize + Paddings.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(
                appIcon = appInfo.appIcon,
                onClick = onClick,
                onLongClick = onLongClick,
                appIconShape = appIconShape,
            )
            if (appInfo.notification && isNotificationBadgeShown) {
                Badge(
                    modifier = Modifier.align(Alignment.TopEnd),
                )
            }
        }
        if (isAppNameShown) {
            Spacer(modifier = Modifier.weight(Weights.Medium))
            LabelSmallText(
                text = appInfo.label,
                overflow = TextOverflow.Ellipsis,
                maxLines = AppItemLabelMaxLines,
            )
        }
    }
}

private const val AdaptiveIconScale = 1.5f

@Suppress("LongMethod")
@Composable
private fun AppIcon(
    appIcon: AppIcon,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,

    appIconShape: Shape = CircleShape,
) {
    when (appIcon.backgroundIcon) {
        is Drawable -> {
            Box(
                modifier = modifier
                    .size(CommonDimensions.AppIconSize)
                    .withmoShadow(
                        shape = appIconShape,
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = appIconShape,
                    )
                    .safeClickable(
                        onClick = onClick,
                        onLongClick = onLongClick,
                    ),
            ) {
                Image(
                    painter = rememberDrawablePainter(drawable = appIcon.backgroundIcon),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.scale(AdaptiveIconScale),
                )
                Image(
                    painter = rememberDrawablePainter(drawable = appIcon.foregroundIcon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.scale(AdaptiveIconScale),
                )
            }
        }

        else -> {
            Box(
                modifier = modifier
                    .size(CommonDimensions.AppIconSize)
                    .withmoShadow(
                        shape = CircleShape,
                    )
                    .background(
                        color = Color.White,
                        shape = CircleShape,
                    )
                    .safeClickable(
                        onClick = onClick,
                        onLongClick = onLongClick,
                    ),
            ) {
                Image(
                    painter = rememberDrawablePainter(drawable = appIcon.foregroundIcon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(CommonDimensions.AppIconSize),
                )
            }
        }
    }
}

@Composable
private fun Badge(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(BadgeSizes.Medium)
            .background(MaterialTheme.colorScheme.primary, CircleShape),
    )
}

@Preview
@Composable
private fun AppLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        App(
            appInfo = AppInfo(
                appIcon = appIcon,
                label = "withmo",
                packageName = "io.github.kei_1111.withmo",
                notification = true,
            ),
            isNotificationBadgeShown = true,
        )
    }
}

@Preview
@Composable
private fun AppDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        App(
            appInfo = AppInfo(
                appIcon = appIcon,
                label = "withmo",
                packageName = "io.github.kei_1111.withmo",
                notification = true,
            ),
            isAppNameShown = false,
            isNotificationBadgeShown = false,
        )
    }
}

@Preview
@Composable
private fun AppIconLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        AppIcon(
            appIcon = appIcon,
            onClick = { },
            onLongClick = { },
        )
    }
}

@Preview
@Composable
private fun AppIconDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        AppIcon(
            appIcon = appIcon,
            onClick = { },
            onLongClick = { },
        )
    }
}

@Preview
@Composable
private fun BadgeLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        Badge()
    }
}

@Preview
@Composable
private fun BadgeDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        Badge()
    }
}
