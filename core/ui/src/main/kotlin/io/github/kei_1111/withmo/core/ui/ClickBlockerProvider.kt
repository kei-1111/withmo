@file:Suppress("MagicNumber")

package io.github.kei_1111.withmo.core.ui

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun ClickBlockerProvider(
    delayMillis: Long = 500L,
    content: @Composable () -> Unit,
) {
    val blocker = remember(delayMillis) { ClickBlocker(delayMillis) }
    CompositionLocalProvider(LocalClickBlocker provides blocker) {
        content()
    }
}

val LocalClickBlocker = staticCompositionLocalOf<ClickBlocker> { ClickBlocker(500L) }

class ClickBlocker(private val delayMillis: Long) {
    private var lastClick = 0L
    fun processClick(action: () -> Unit) {
        val now = SystemClock.uptimeMillis()
        if (now - lastClick >= delayMillis) {
            lastClick = now
            action()
        }
    }
}
