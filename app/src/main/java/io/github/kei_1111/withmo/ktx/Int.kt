package io.github.kei_1111.withmo.ktx

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun Int.toPx(): Float {
    val density = LocalDensity.current
    return with(density) { this@toPx.dp.toPx() }
}
