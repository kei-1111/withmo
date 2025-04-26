package io.github.kei_1111.withmo.ui

import android.os.Build
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import io.github.kei_1111.withmo.common.unity.UnityManager
import io.github.kei_1111.withmo.navigation.Screen
import io.github.kei_1111.withmo.navigation.WithmoNavHost

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing")
@Composable
fun App(
    startScreen: Screen,
) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        UnityView(
            modifier = Modifier.fillMaxSize(),
        )

        WithmoNavHost(
            navController = navController,
            startDestination = startScreen,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun UnityView(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val unityView = remember { UnityManager.player.view }
    val container = remember { FrameLayout(context) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> UnityManager.resume()
                Lifecycle.Event.ON_STOP -> UnityManager.pause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    AndroidView(
        factory = { container },
        update = { host ->
            if (unityView.parent != host) {
                (unityView.parent as? ViewGroup)?.removeView(unityView)
                host.addView(
                    unityView,
                    ViewGroup.LayoutParams(
                        MATCH_PARENT,
                        MATCH_PARENT,
                    ),
                )
            }
        },
        modifier = modifier.fillMaxSize(),
    )
}
