@file:Suppress("Filename", "MatchingDeclarationName")

package io.github.kei_1111.withmo.core.designsystem.component.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class WithmoShadows(
    val small: Shadow = Shadow(
        radius = 2.dp,
        color = Color.Black.copy(alpha = 0.15f),
    ),
    val medium: Shadow = Shadow(
        radius = 4.dp,
        color = Color.Black.copy(alpha = 0.2f),
    ),
    val large: Shadow = Shadow(
        radius = 8.dp,
        color = Color.Black.copy(alpha = 0.25f),
    ),
) {
    fun custom(
        radius: Dp,
        color: Color = Color.Black.copy(alpha = 0.2f),
    ): Shadow = Shadow(
        radius = radius,
        color = color,
    )
}
