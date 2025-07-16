package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemDarkPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemLightPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.ShadowElevations
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

private val TopAppBarHeight = 64.dp
private val RippleRadius = 24.dp

@Composable
fun WithmoTopAppBar(
    modifier: Modifier = Modifier,
    navigateBack: (() -> Unit)? = null,
    navigateClose: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val topPaddingValue = WindowInsets.safeGestures.asPaddingValues().calculateTopPadding()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(TopAppBarHeight + topPaddingValue),
        shadowElevation = ShadowElevations.Medium,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPaddingValue)
                .padding(horizontal = Paddings.Medium),
            contentAlignment = Alignment.CenterStart,
        ) {
            navigateBack?.let {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.safeClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            bounded = false,
                            radius = RippleRadius,
                        ),
                    ) { navigateBack() },
                )
            }
            navigateClose?.let {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.safeClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            bounded = false,
                            radius = RippleRadius,
                        ),
                    ) { navigateClose() },
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun WithmoTopAppBarLightPreview() {
    DesignSystemLightPreviewEnvironment {
        WithmoTopAppBar(
            navigateBack = {},
            content = {
                TitleLargeText("Withmo Top App Bar")
            },
        )
    }
}

@Preview
@Composable
private fun WithmoTopAppBarDarkPreview() {
    DesignSystemDarkPreviewEnvironment {
        WithmoTopAppBar(
            navigateBack = {},
            content = {
                TitleLargeText("Withmo Top App Bar")
            },
        )
    }
}
