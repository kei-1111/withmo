package io.github.kei_1111.withmo.ui

import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
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
import io.github.kei_1111.withmo.core.common.unity.UnityManager
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.ui.navigation.NavigationRoute
import io.github.kei_1111.withmo.navigation.WithmoNavHost

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing")
@Composable
fun App(
    startDestination: NavigationRoute,
) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WithmoTheme.colorScheme.surface,
    ) {
        UnityView(
            modifier = Modifier.fillMaxSize(),
        )

        WithmoNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun UnityView(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val unitySurfaceView = remember {
        SurfaceView(context).apply { holder.addCallback(HolderCallback()) }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> UnityManager.resumeForActivity()

                Lifecycle.Event.ON_STOP -> UnityManager.pauseForActivity()

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    AndroidView(
        factory = { unitySurfaceView },
        modifier = modifier.fillMaxSize(),
    )
}

private class HolderCallback : SurfaceHolder.Callback {

    override fun surfaceCreated(h: SurfaceHolder) {
        UnityManager.attachSurfaceForActivity(h.surface)
        UnityManager.resumeForActivity()
        UnityManager.focusGainedForActivity(true)
    }

    override fun surfaceDestroyed(h: SurfaceHolder) {
        UnityManager.pauseForActivity()
        UnityManager.detachSurfaceForActivity()
    }

    @Suppress("EmptyFunctionBlock")
    override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, hgt: Int) {}
}
