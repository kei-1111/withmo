package io.github.kei_1111.withmo.core.util.ktx

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

// WidgetのサイズはPx用の値として保存されており、Dpに変換する必要がある
// Px用の値と言っても、Intで保存されているため、Intの拡張関数として定義する
// .dpは、値をそのままDpに変換してしまうため不適
@Composable
fun Int.toDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@toDp.toDp() }
}
