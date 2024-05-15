package com.example.withmo.ui.component.settingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ChoosingHome() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50.dp))
                .weight(1f)
                .clip(RoundedCornerShape(50.dp))
                .height(3.dp)
        )
        Divider(
            modifier = Modifier
                .weight(1f),
            color = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun ChoosingModel() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Divider(
            modifier = Modifier
                .weight(1f),
            color = MaterialTheme.colorScheme.surface
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50.dp))
                .weight(1f)
                .clip(RoundedCornerShape(50.dp))
                .height(3.dp)
        )
    }
}