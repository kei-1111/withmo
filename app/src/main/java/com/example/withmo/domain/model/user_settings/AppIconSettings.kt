package com.example.withmo.domain.model.user_settings

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.example.withmo.ui.theme.UiConfig

data class AppIconSettings(
    val appIconSize: Float = UiConfig.DefaultAppIconSize,
    val appIconShape: AppIconShape = AppIconShape.Circle,
    val roundedCornerPercent: Float = UiConfig.DefaultRoundedCornerPercent,
    val isAppNameShown: Boolean = true,
)

sealed class AppIconShape {
    data object Circle : AppIconShape()
    data object RoundedCorner : AppIconShape()
    data object Square : AppIconShape()
}

fun AppIconShape.toShape(roundedCornerPercent: Float): Shape = when (this) {
    AppIconShape.Circle -> CircleShape
    AppIconShape.RoundedCorner -> RoundedCornerShape(roundedCornerPercent)
    AppIconShape.Square -> RectangleShape
}
