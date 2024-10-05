@file:Suppress("MagicNumber")

package com.example.withmo.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(20.dp),
)

val BottomSheetShape = RoundedCornerShape(
    topStart = 10.dp,
    topEnd = 10.dp,
)
