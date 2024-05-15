package com.example.withmo.ui.component.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.example.withmo.R
import com.example.withmo.domain.model.ClockMode
import com.example.withmo.until.CONTENT_PADDING
import com.example.withmo.until.MEDIUM_SPACE
import com.example.withmo.until.SMALL_SPACE

@Composable
fun Clock(
    clockMode: ClockMode,
    year: String,
    month: String,
    day: String,
    dayOfWeek: String,
    hour: String,
    minute: String,
) {
    val clockTextSmallestSize = TextStyle(
        fontFamily = FontFamily(Font(R.font.biz_ud_gothic_bold)),
        fontSize = 10.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both
        )
    )

    val clockTextSmallSize = TextStyle(
        fontFamily = FontFamily(Font(R.font.biz_ud_gothic_bold)),
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both
        )
    )

    val clockTextMediumSize = TextStyle(
        fontFamily = FontFamily(Font(R.font.biz_ud_gothic_bold)),
        fontSize = 22.8.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both
        )
    )

    val clockTextLargeSize = TextStyle(
        fontFamily = FontFamily(Font(R.font.biz_ud_gothic_bold)),
        fontSize = 37.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both
        )
    )

    when (clockMode) {
        ClockMode.TOP_DATE -> {
            Column(
                modifier = Modifier
                    .background(Color.Transparent),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SMALL_SPACE)
                ) {
                    Text(
                        text = year,
                        style = clockTextSmallSize
                    )
                    Text(
                        text = month,
                        style = clockTextSmallSize
                    )
                    Text(
                        text = day,
                        style = clockTextSmallSize
                    )
                    Text(
                        text = dayOfWeek,
                        style = clockTextSmallSize
                    )
                }
                Spacer(modifier = Modifier.height(SMALL_SPACE))
                Row {
                    Text(
                        text = "$hour:",
                        style = clockTextLargeSize
                    )
                    Text(
                        text = minute,
                        style = clockTextLargeSize
                    )
                }
            }
        }

        ClockMode.HORIZONTAL_DATE -> {
            Column {
                Row(
                    modifier = Modifier
                        .background(Color.Transparent),
                ) {
                    Column {
                        Text(
                            text = year,
                            style = clockTextSmallestSize
                        )
                        Text(
                            text = month,
                            style = clockTextMediumSize
                        )
                        Text(
                            text = day,
                            style = clockTextMediumSize
                        )
                    }
                    Spacer(modifier = Modifier.width(MEDIUM_SPACE))
                    Column {
                        Text(
                            text = dayOfWeek,
                            style = clockTextSmallSize
                        )
                        Spacer(modifier = Modifier.height(SMALL_SPACE))
                        Row {
                            Text(
                                text = "$hour:",
                                style = clockTextLargeSize
                            )
                            Text(
                                text = minute,
                                style = clockTextLargeSize
                            )
                        }
                    }
                }
            }
        }
    }
}