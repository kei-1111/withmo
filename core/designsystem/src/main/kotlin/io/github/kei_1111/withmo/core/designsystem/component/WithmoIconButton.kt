package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.core.designsystem.component.utils.withmoShadow
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

@Composable
fun WithmoIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .withmoShadow(
                shape = CircleShape,
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape,
            )
            .safeClickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
