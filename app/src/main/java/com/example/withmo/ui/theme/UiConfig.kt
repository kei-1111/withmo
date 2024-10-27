@file:Suppress("MagicNumber")

package com.example.withmo.ui.theme

import androidx.compose.ui.unit.dp

data object UiConfig {
    const val DefaultWeight = 1f

    const val SwitchScale = 0.75f

    const val DefaultAppIconSize = 48f
    const val MinAppIconSize = 36f
    const val MaxAppIconSize = 60f

    const val DefaultAppIconHorizontalSpacing = 10f
    const val MinAppIconHorizontalSpacing = 0f
    const val MaxAppIconHorizontalSpacing = 20f

    const val DefaultRoundedCornerPercent = 20f
    const val MinRoundedCornerPercent = 0f
    const val MaxRoundedCornerPercent = 100f

    const val AppInfoDefaultUseCount = 0

    const val AppIconPadding = 22f
    const val AppIconTextMaxLines = 1

    const val AppListScreenGridColums = 4

    const val DisabledContentAlpha = 0.38f

    const val AdaptiveIconScale = 1.5f

    const val BottomSheetShowDragHeight = -50f

    const val PageCount = 2

    const val FavoriteAppListMaxSize = 4

//    App Icon Settings
    val AppIconPreviewHeight = 200.dp

//    Paddings
    val TinyPadding = 2.dp
    val ExtraSmallPadding = 5.dp
    val SmallPadding = 10.dp
    val MediumPadding = 15.dp
    val LargePadding = 20.dp

//    Shadow
    val ShadowElevation = 5.dp

    val PopupWidth = 400.dp

    val BadgeSize = 15.dp

    val TextFieldHeight = 36.dp
    val TopAppBarHeight = 64.dp

//    SettingsScreen
    val SettingsScreenItemIconSize = 24.dp

    val SettingItemHeight = 56.dp

    val BorderWidth = 1.dp

//    Slider
    val SliderThumbSize = 20.dp
    val SliderTrackHeight = 4.dp
    val SliderShadowElevation = 1.dp

    val PageIndicatorSpaceHeight = 25.dp
    val PageIndicatorSize = 8.dp

    val OnboardingImageSize = 250.dp
}
