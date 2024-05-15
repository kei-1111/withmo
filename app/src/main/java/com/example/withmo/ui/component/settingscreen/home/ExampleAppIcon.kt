package com.example.withmo.ui.component.settingscreen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.withmo.R
import com.example.withmo.ui.theme.Typography

@Composable
fun ExampleAppIcon(
    appIconSize: Float,
    appIconPadding: Float,
    showAppName: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .size((appIconSize + 22f).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.withmo_icon_wide),
                contentDescription = null,
                modifier = Modifier
                    .size(appIconSize.dp)
                    .shadow(5.dp, CircleShape)
                    .clip(CircleShape)
            )
            if (showAppName) {
                Text(
                    text = "Withmo",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.width(appIconPadding.dp))
        Column(
            modifier = Modifier
                .size((appIconSize + 22f).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.withmo_icon_wide),
                contentDescription = null,
                modifier = Modifier
                    .size(appIconSize.dp)
                    .shadow(5.dp, CircleShape)
                    .clip(CircleShape)
            )
            if (showAppName) {
                Text(
                    text = "Withmo",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.bodyMedium
                )
            }
        }
    }
}