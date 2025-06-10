package io.github.kei_1111.withmo.core.ui

import androidx.compose.runtime.Composable

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
