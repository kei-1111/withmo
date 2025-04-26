package io.github.kei_1111.withmo.ui.component

import android.graphics.drawable.Drawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.github.kei_1111.withmo.common.AppConstants
import io.github.kei_1111.withmo.domain.model.AppIcon
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.theme.dimensions.BadgeSizes
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.ShadowElevations
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

private const val AppItemLabelMaxLines = 1

@OptIn(ExperimentalFoundationApi::class)
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
                .size((appIconSize + Paddings.AppIconPadding).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AppIcon(
                appIcon = appInfo.appIcon,
                appIconSize = appIconSize,
                appIconShape = appIconShape,
                modifier = Modifier
                    .clip(CircleShape)
                    .combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongClick,
                    ),
            )
            if (isAppNameShown) {
                Spacer(modifier = Modifier.weight(Weights.Medium))
                LabelMediumText(
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

@Composable
private fun AppIcon(
    appIcon: AppIcon,
    modifier: Modifier = Modifier,
    appIconSize: Float = AppConstants.DefaultAppIconSize,
    appIconShape: Shape = CircleShape,
) {
    when (appIcon.backgroundIcon) {
        is Drawable -> {
            Surface(
                modifier = modifier.size(appIconSize.dp),
                color = Color.White,
                shape = appIconShape,
                shadowElevation = ShadowElevations.Medium,
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
            Surface(
                modifier = modifier.size(appIconSize.dp),
                color = Color.White,
                shape = CircleShape,
                shadowElevation = ShadowElevations.Medium,
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
