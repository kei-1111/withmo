@file:Suppress("MagicNumber")

package io.github.kei_1111.withmo.core.designsystem.component.theme

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import io.github.kei_1111.withmo.core.designsystem.R

val NotoSansJp = FontFamily(
    Font(R.font.noto_sans_jp_medium, FontWeight.Medium),
    Font(R.font.noto_sans_jp_semi_bold, FontWeight.SemiBold),
    Font(R.font.noto_sans_jp_black, FontWeight.Black),
)

val OpenSans = FontFamily(
    Font(R.font.open_sans_light, FontWeight.Light),
)

data class WithmoTypography(
    val displayMedium: TextStyle,
    val titleLarge: TextStyle,
    val bodyMedium: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
)

val Typography = WithmoTypography(
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
    labelSmall = TextStyle(
        fontFamily = NotoSansJp,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 12.sp,
    ),
)

val clockTextExtraSmallSize = TextStyle(
    fontFamily = OpenSans,
    fontWeight = FontWeight.Light,
    fontSize = 10.sp,
    lineHeight = 10.sp,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)

val clockTextSmallSize = TextStyle(
    fontFamily = OpenSans,
    fontWeight = FontWeight.Light,
    fontSize = 15.sp,
    lineHeight = 15.sp,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)

val clockTextMediumSize = TextStyle(
    fontFamily = OpenSans,
    fontWeight = FontWeight.Light,
    fontSize = 19.sp,
    lineHeight = 19.sp,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)

val clockTextLargeSize = TextStyle(
    fontFamily = OpenSans,
    fontWeight = FontWeight.Light,
    fontSize = 44.sp,
    lineHeight = 44.sp,
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both,
    ),
)
