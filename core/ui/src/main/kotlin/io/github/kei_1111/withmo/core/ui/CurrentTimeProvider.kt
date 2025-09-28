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

private const val ONE_SECOND_MILLIS = 1000L
private const val NANO_TO_MILLI_DIVISOR = 1_000_000L
private const val DELAY_THRESHOLD = 200L

@Composable
fun CurrentTimeProvider(content: @Composable () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(null) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val dateTime = Instant.now().atZone(ZoneId.systemDefault())

                currentTime = dateTime.toEpochSecond() * ONE_SECOND_MILLIS

                val millis = dateTime.nano / NANO_TO_MILLI_DIVISOR
                var next = ONE_SECOND_MILLIS - millis
                if (next <= DELAY_THRESHOLD) next += ONE_SECOND_MILLIS

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

val LocalCurrentTime = compositionLocalOf<ZonedDateTime> { ZonedDateTime.now() }
