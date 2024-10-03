package com.example.withmo.ui

import android.os.Build
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.screens.home.HomeScreen
import com.example.withmo.ui.screens.setting.SettingScreen
import com.unity3d.player.UnityPlayer
import kotlinx.collections.immutable.ImmutableList

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AppContent(
    uiState: Boolean,
    unityPlayer: UnityPlayer?,
    navigateToSettingScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    appList: ImmutableList<AppInfo>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
    ) {
        unityPlayer?.let {
            UnityScreen(
                unityPlayer = unityPlayer,
                modifier = Modifier.fillMaxSize(),
            )
        }

        AnimatedVisibility(
            visible = !uiState,
        ) {
            HomeScreen(
                navigateToSettingScreen = navigateToSettingScreen,
                appList = appList,
            )
        }

        AnimatedVisibility(
            visible = uiState,
            enter = slideInVertically(
                initialOffsetY = { it },
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
            ),
        ) {
            SettingScreen(
                navigateToHomeScreen = navigateToHomeScreen,
            )
        }
    }
}

@Composable
private fun UnityScreen(
    unityPlayer: UnityPlayer,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            FrameLayout(context).apply {
                unityPlayer?.let { addView(it.rootView) }
            }
        },
    )
}
