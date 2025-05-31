package io.github.kei_1111.withmo.core.common

// dataモジュールやfeatureモジュールなど分離すべきモジュールで使用する定数を定義
data object AppConstants {
    const val FavoriteAppListMaxSize = 4

    const val DefaultAppIconSize = 48f
    const val MinAppIconSize = 36f
    const val MaxAppIconSize = 60f

    const val DefaultRoundedCornerPercent = 20f
    const val MinRoundedCornerPercent = 0f
    const val MaxRoundedCornerPercent = 100f
}
