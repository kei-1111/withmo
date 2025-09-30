package io.github.kei_1111.withmo.core.designsystem.component

import android.R.attr.onClick
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

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
            .height(64.dp + topPaddingValue),
        color = WithmoTheme.colorScheme.surface,
        shadowElevation = 5.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPaddingValue)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            navigateBack?.let {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = WithmoTheme.colorScheme.onSurface,
                    modifier = Modifier.safeClickable(
                        indication = ripple(
                            bounded = false,
                            radius = 24.dp,
                        ),
                        onClick = navigateBack,
                    ),
                )
            }
            navigateClose?.let {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = WithmoTheme.colorScheme.onSurface,
                    modifier = Modifier.safeClickable(
                        indication = ripple(
                            bounded = false,
                            radius = 24.dp,
                        ),
                        onClick = navigateClose,
                    ),
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
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoTopAppBar(
            navigateBack = {},
            content = {
                Text(
                    text = "Withmo Top App Bar",
                    color = WithmoTheme.colorScheme.onSurface,
                    style = WithmoTheme.typography.titleLarge,
                )
            },
        )
    }
}

@Preview
@Composable
private fun WithmoTopAppBarDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoTopAppBar(
            navigateBack = {},
            content = {
                Text(
                    text = "Withmo Top App Bar",
                    color = WithmoTheme.colorScheme.onSurface,
                    style = WithmoTheme.typography.titleLarge,
                )
            },
        )
    }
}
