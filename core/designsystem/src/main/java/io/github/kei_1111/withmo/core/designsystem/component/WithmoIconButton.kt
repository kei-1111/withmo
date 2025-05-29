package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.core.designsystem.component.utils.withmoShadow

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
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
