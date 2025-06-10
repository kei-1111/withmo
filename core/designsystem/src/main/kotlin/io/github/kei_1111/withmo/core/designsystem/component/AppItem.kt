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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.designsystem.R
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.BadgeSizes
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.ui.PreviewEnvironment
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

private const val AppItemLabelMaxLines = 1

@Composable
fun AppItem(
    appInfo: AppInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    appIconSize: Float = AppConstants.DefaultAppIconSize,
    appIconShape: Shape = CircleShape,
    isAppNameShown: Boolean = true,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .size(appIconSize.dp + Paddings.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AppIcon(
                appIcon = appInfo.appIcon,
                onClick = onClick,
                onLongClick = onLongClick,
                appIconSize = appIconSize,
                appIconShape = appIconShape,
            )
            if (isAppNameShown) {
                Spacer(modifier = Modifier.weight(Weights.Medium))
                LabelSmallText(
                    text = appInfo.label,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = AppItemLabelMaxLines,
                )
            }
        }
        if (appInfo.notification) {
            Box(
                modifier = Modifier
                    .size(appIconSize.dp)
                    .align(Alignment.TopCenter),
            ) {
                Badge(
                    modifier = Modifier.align(Alignment.TopEnd),
                )
            }
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
    appIconSize: Float = AppConstants.DefaultAppIconSize,
    appIconShape: Shape = CircleShape,
) {
    when (appIcon.backgroundIcon) {
        is Drawable -> {
            Box(
                modifier = modifier
                    .size(appIconSize.dp)
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
                    .size(appIconSize.dp)
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
                    modifier = Modifier.size(appIconSize.dp),
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

@Composable
@Preview
private fun AppItemPreview() {
    PreviewEnvironment {
        val context = LocalContext.current
        val appIcon = remember {
            AppIcon(
                foregroundIcon = ContextCompat.getDrawable(context, R.drawable.withmo_icon_wide)!!,
                backgroundIcon = null,
            )
        }

        AppItem(
            appInfo = AppInfo(
                appIcon = appIcon,
                label = "withmo",
                packageName = "io.github.kei_1111.withmo",
                notification = true,
            ),
        )
    }
}

@Composable
@Preview
private fun AppIconPreview() {
    PreviewEnvironment {
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

@Composable
@Preview
private fun BadgePreview() {
    Badge()
}
