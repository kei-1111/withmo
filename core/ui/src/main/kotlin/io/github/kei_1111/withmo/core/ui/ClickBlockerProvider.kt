@file:Suppress("MagicNumber")

package io.github.kei_1111.withmo.core.ui

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun ClickBlockerProvider(
    delayMillis: Long = 200L,
    content: @Composable () -> Unit
) {
    val blocker = remember(delayMillis) { ClickBlocker(delayMillis) }
    CompositionLocalProvider(LocalClickBlocker provides blocker) {
        content()
    }
}

val LocalClickBlocker = staticCompositionLocalOf<ClickBlocker> { error("ClickBlocker not provided") }

class ClickBlocker(private val delayMillis: Long) {
    private var lastClick = 0L
    fun tryClick(): Boolean {
        val now = SystemClock.uptimeMillis()
        return if (now - lastClick >= delayMillis) {
            lastClick = now
            true
        } else {
            false
        }
    }
}

