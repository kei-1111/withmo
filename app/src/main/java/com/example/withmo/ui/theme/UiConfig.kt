@file:Suppress("MagicNumber")

package com.example.withmo.ui.theme

import androidx.compose.ui.unit.dp

data object UiConfig {
    const val DefaultWeight = 1f

    const val SwitchScale = 0.75f

    const val DefaultAppIconSize = 48f
    const val MinAppIconSize = 36f
    const val MaxAppIconSize = 72f

    const val DefaultAppIconHorizontalSpacing = 10f
    const val MinAppIconHorizontalSpacing = 0f
    const val MaxAppIconHorizontalSpacing = 20f

    const val DefaultRoundedCornerPercent = 20f
    const val MinRoundedCornerPercent = 0f
    const val MaxRoundedCornerPercent = 100f

    const val AppInfoDefaultUseCount = 0

    const val AppIconPadding = 22f
    const val AppIconTextMaxLines = 1

    const val GoldenRatio = 1.618f

    const val AppListScreenGridColums = 4

    const val DisabledContentAlpha = 0.38f

    const val AdaptiveIconScale = 1.5f

    const val BottomSheetShowDragHeight = -50f

//    App Icon Settings
    val AppIconPreviewHeight = 200.dp

//    Paddings
    val ExtraSmallPadding = 5.dp
    val SmallPadding = 10.dp
    val MediumPadding = 15.dp
    val LargePadding = 20.dp

//    Shadow
    val ShadowElevation = 5.dp

//    Tonal
    val TonalElevation = 5.dp

    val PopupWidth = 400.dp

    val BadgeSize = 15.dp

    val DividerHeight = 3.dp

    val TextFieldHeight = 36.dp
    val TopAppBarHeight = 64.dp

//    SettingsScreen
    val SettingsScreenItemIconSize = 24.dp

    val SettingItemHeight = 56.dp

    val BorderWidth = 1.dp
}
