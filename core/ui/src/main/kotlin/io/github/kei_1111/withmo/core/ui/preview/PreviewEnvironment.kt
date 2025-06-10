package io.github.kei_1111.withmo.core.ui.preview

import androidx.compose.runtime.Composable
import io.github.kei_1111.withmo.core.ui.ClickBlockerProvider
import io.github.kei_1111.withmo.core.ui.CurrentTimeProvider

@Composable
fun PreviewEnvironment(
    content: @Composable () -> Unit,
) {
    ClickBlockerProvider {
        CurrentTimeProvider {
            content()
        }
    }
}
