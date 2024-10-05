package com.example.withmo.ui

import android.os.Build
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.screens.home.HomeScreen
import com.example.withmo.ui.screens.setting.SettingsScreen
import com.unity3d.player.UnityPlayer
import kotlinx.collections.immutable.ImmutableList

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AppContent(
    unityPlayer: UnityPlayer?,
    appList: ImmutableList<AppInfo>,
    modifier: Modifier = Modifier,
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    Surface(
        modifier = modifier,
    ) {
        unityPlayer?.let {
            UnityScreen(
                unityPlayer = unityPlayer,
                modifier = Modifier.fillMaxSize(),
            )
        }

        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                when (currentScreen) {
                    is Screen.Home -> {
                        fadeIn().togetherWith(slideOutVertically { it })
                    }

                    is Screen.Settings -> {
                        slideInVertically { it }.togetherWith(fadeOut())
                    }
                }
            },
        ) { targetState ->
            when (targetState) {
                is Screen.Home -> {
                    HomeScreen(
                        navigateToSettingScreen = { currentScreen = Screen.Settings },
                        appList = appList,
                    )
                }

                is Screen.Settings -> {
                    SettingsScreen(
                        navigateToHomeScreen = { currentScreen = Screen.Home },
                    )
                }
            }
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
