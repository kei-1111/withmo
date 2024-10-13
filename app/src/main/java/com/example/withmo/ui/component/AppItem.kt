package com.example.withmo.ui.component

import android.content.Context
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.withmo.domain.model.AppIcon
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.theme.UiConfig
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(
    context: Context,
    appInfo: AppInfo,
    navigateToSettingScreen: () -> Unit,
    modifier: Modifier = Modifier,
    appIconSize: Float = UiConfig.DefaultAppIconSize,
    appIconShape: Shape = CircleShape,
    isAppNameShown: Boolean = true,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .size((appIconSize + UiConfig.AppIconPadding).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AppIcon(
                appIcon = appInfo.appIcon,
                appIconSize = appIconSize,
                appIconShape = appIconShape,
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            if (appInfo.packageName == context.packageName) {
                                navigateToSettingScreen()
                            } else {
                                appInfo.launch(context = context)
                            }
                        },
                        onLongClick = {
                            if (appInfo.packageName != context.packageName) appInfo.delete(context = context)
                        },
                    ),
            )
            if (isAppNameShown) {
                Spacer(modifier = Modifier.weight(UiConfig.DefaultWeight))
                LabelMediumText(text = appInfo.label)
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

@Composable
private fun AppIcon(
    appIcon: AppIcon,
    modifier: Modifier = Modifier,
    appIconSize: Float = UiConfig.DefaultAppIconSize,
    appIconShape: Shape = CircleShape,
) {
    when (appIcon.backgroundIcon) {
        is Drawable -> {
            Surface(
                modifier = modifier.size(appIconSize.dp),
                color = Color.White,
                shape = appIconShape,
                shadowElevation = UiConfig.ShadowElevation,
            ) {
                Image(
                    painter = rememberDrawablePainter(drawable = appIcon.backgroundIcon),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.scale(UiConfig.AdaptiveIconScale),
                )
                Image(
                    painter = rememberDrawablePainter(drawable = appIcon.foregroundIcon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.scale(UiConfig.AdaptiveIconScale),
                )
            }
        }

        else -> {
            Surface(
                modifier = modifier.size(appIconSize.dp),
                color = Color.White,
                shape = CircleShape,
                shadowElevation = UiConfig.ShadowElevation,
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
            .size(UiConfig.BadgeSize)
            .background(MaterialTheme.colorScheme.primary, CircleShape),
    )
}
