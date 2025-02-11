@file:Suppress("MagicNumber")

package io.github.kei_1111.withmo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import io.github.kei_1111.withmo.R

val NotoSansJp = FontFamily(
    Font(R.font.noto_sans_jp_medium, FontWeight.Medium),
    Font(R.font.noto_sans_jp_semi_bold, FontWeight.SemiBold),
    Font(R.font.noto_sans_jp_black, FontWeight.Black),
)

val BizUdGothic = FontFamily(
    Font(R.font.biz_ud_gothic_bold, FontWeight.Bold),
)

val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.Black,
        fontSize = 42.sp,
        lineHeight = 52.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),

    bodyMedium = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),

    labelMedium = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 16.sp,
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
