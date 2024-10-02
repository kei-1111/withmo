@file:Suppress("MagicNumber")

package com.example.withmo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.example.withmo.R

val NotoSansJp = FontFamily(
    Font(R.font.noto_sans_jp_medium, FontWeight.Medium),
    Font(R.font.noto_sans_jp_semi_bold, FontWeight.SemiBold),
    Font(R.font.noto_sans_jp_black, FontWeight.Black),
)

val BizUdGothic = FontFamily(
    Font(R.font.biz_ud_gothic_bold, FontWeight.Bold),
)

val Typography = Typography(
    headlineMedium = TextStyle(
        fontFamily = NotoSansJp,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    ),
    titleLarge = TextStyle(
        fontFamily = NotoSansJp,
        fontSize = 48.sp,
        fontWeight = FontWeight.Black,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    ),
    titleMedium = TextStyle(
        fontFamily = NotoSansJp,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    ),
    bodyMedium = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.Medium,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    ),
)

val clockTextExtraSmallSize = TextStyle(
    fontFamily = BizUdGothic,
    fontWeight = FontWeight.Bold,
    fontSize = 10.sp,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)

val clockTextSmallSize = TextStyle(
    fontFamily = BizUdGothic,
    fontWeight = FontWeight.Bold,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)

val clockTextMediumSize = TextStyle(
    fontFamily = BizUdGothic,
    fontWeight = FontWeight.Bold,
    fontSize = 22.8.sp,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)

val clockTextLargeSize = TextStyle(
    fontFamily = BizUdGothic,
    fontWeight = FontWeight.Bold,
    fontSize = 37.sp,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)
