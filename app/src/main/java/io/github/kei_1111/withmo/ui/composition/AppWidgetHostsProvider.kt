package io.github.kei_1111.withmo.ui.composition

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun AppWidgetHostsProvider(
    appWidgetHost: AppWidgetHost,
    appWidgetManager: AppWidgetManager,
    content: @Composable () -> Unit,
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(appWidgetHost, lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> appWidgetHost.startListening()
                Lifecycle.Event.ON_STOP -> appWidgetHost.stopListening()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    CompositionLocalProvider(
        LocalAppWidgetHost provides appWidgetHost,
        LocalAppWidgetManager provides appWidgetManager,
    ) {
        content()
    }
}

val LocalAppWidgetHost = staticCompositionLocalOf<AppWidgetHost> { error("AppWidgetHost not provided") }

val LocalAppWidgetManager = staticCompositionLocalOf<AppWidgetManager> { error("AppWidgetManager not provided") }
