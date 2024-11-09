package com.example.withmo.ui.component

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withmo.ui.theme.UiConfig

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
            .height(UiConfig.TopAppBarHeight + topPaddingValue),
        shadowElevation = UiConfig.ShadowElevation,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPaddingValue)
                .padding(horizontal = UiConfig.MediumPadding),
            contentAlignment = Alignment.CenterStart,
        ) {
            navigateBack?.let {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { navigateBack() },
                )
            }
            navigateClose?.let {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { navigateClose() },
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
