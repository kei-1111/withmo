package com.example.withmo.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.withmo.R
import com.example.withmo.ui.theme.UiConfig

@Composable
fun LogoWithText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.withmo_logo),
            contentDescription = "withmo Logo",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(
                    start = UiConfig.ExtraSmallPadding,
                    end = UiConfig.ExtraSmallPadding,
                    top = UiConfig.SmallPadding,
                    bottom = UiConfig.ExtraSmallPadding,
                ),
        )
        TitleLargeText(text = text)
    }
}
