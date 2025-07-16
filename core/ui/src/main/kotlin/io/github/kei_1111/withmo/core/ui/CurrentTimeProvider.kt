package io.github.kei_1111.withmo.core.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import io.github.kei_1111.withmo.core.util.ktx.toZonedDateTime
import kotlinx.coroutines.awaitCancellation
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.coroutines.cancellation.CancellationException

private const val OneSecondMillis = 1000L
private const val NanoToMilliDivisor = 1_000_000L
private const val DelayThreshold = 200L

@Composable
fun CurrentTimeProvider(content: @Composable () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(null) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val dateTime = Instant.now().atZone(ZoneId.systemDefault())

                currentTime = dateTime.toEpochSecond() * OneSecondMillis

                val millis = dateTime.nano / NanoToMilliDivisor
                var next = OneSecondMillis - millis
                if (next <= DelayThreshold) next += OneSecondMillis

                handler.postDelayed(this, next)
            }
        }
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            handler.post(runnable)
            try {
                awaitCancellation()
            } catch (e: CancellationException) {
                Log.e("CurrentTimeProvider", "Cancelled", e)
                handler.removeCallbacks(runnable)
            }
        }
    }

    CompositionLocalProvider(
        LocalCurrentTime provides currentTime.toZonedDateTime(),
    ) {
        content()
    }
}

val LocalCurrentTime = compositionLocalOf<ZonedDateTime> { error("No current time provided") }
